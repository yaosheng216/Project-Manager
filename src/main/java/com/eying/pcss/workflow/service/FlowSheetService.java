package com.eying.pcss.workflow.service;

import com.eying.pcss.core.exception.GenExceptCode;
import com.eying.pcss.core.exception.ServiceException;
import com.eying.pcss.core.service.EventTypes;
import com.eying.pcss.core.util.CommonUtil;
import com.eying.pcss.core.util.CopyUtil;
import com.eying.pcss.core.util.DateFormatUtil;
import com.eying.pcss.workflow.bo.FlowSheetBO;
import com.eying.pcss.workflow.constant.*;
import com.eying.pcss.workflow.dao.FlowDefinitionDao;
import com.eying.pcss.workflow.dao.FlowSheetDao;
import com.eying.pcss.workflow.dao.WorkFlowAppDao;
import com.eying.pcss.workflow.dto.*;
import com.eying.pcss.workflow.entity.*;
import com.eying.pcss.workflow.entity.QFlowSheet;
import com.eying.pcss.workflow.util.ActivitiUtils;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.jsonwebtoken.lang.Assert;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.util.json.JSONML;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 审批单Service类
 */
@Service
public class FlowSheetService {

    private final FlowSheetDao flowSheetDao;
    private final WorkFlowAppDao workFlowAppDao;
    private final FlowDefinitionDao flowDefinitionDao;
    private final FindStaffService findStaffService;
    private final FindRolesService findRolesService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final RepositoryService repositoryService;
    private final HistoryService historyService;
    private final LocalContainerEntityManagerFactoryBean entityManagerFactory;
    private final FileService fileService;
    private final StringRedisTemplate redisTemplate;

    private static Pattern humpPattern = Pattern.compile ("[A-Z]");

    @Autowired
    public FlowSheetService(FindRolesService findRolesService, FlowSheetDao flowSheetDao,
                            WorkFlowAppDao workFlowAppDao, FlowDefinitionDao flowDefinitionDao,
                            FindStaffService findStaffService, RuntimeService runtimeService,
                            StringRedisTemplate redisTemplate, TaskService taskService,
                            RepositoryService repositoryService, HistoryService historyService,
                            LocalContainerEntityManagerFactoryBean entityManagerFactory, FileService fileService) {
        this.findRolesService = findRolesService;
        this.flowSheetDao = flowSheetDao;
        this.workFlowAppDao = workFlowAppDao;
        this.flowDefinitionDao = flowDefinitionDao;
        this.findStaffService = findStaffService;
        this.runtimeService = runtimeService;
        this.redisTemplate = redisTemplate;
        this.taskService = taskService;
        this.repositoryService = repositoryService;
        this.historyService = historyService;
        this.entityManagerFactory = entityManagerFactory;
        this.fileService = fileService;
    }

    /**
     * 发起审批
     *
     * @param sheetBO 审批单输入对象
     * @return 返回审批单id
     */
    public String createInstance(FlowSheetBO sheetBO) {
        FlowSheet flowSheet = new FlowSheet ();
        CopyUtil.copyNotNullProperties (sheetBO, flowSheet);
        flowSheet.setSubmitTime (LocalDateTime.now ());
        // 生成审批编号
        String sheetNumber = createSheetNumber ();
        flowSheet.setSheetNumber (sheetNumber);
        transplantAttach (flowSheet);
        //生成流程引擎实例
        return crateProcessInstance (flowSheet, false);
    }

    /**
     * 生成审批编号，不能重复，格式为：SP+yyyyMMddHHmmss+3位随机数的编码，例如SP20190527183410001
     *
     * @return
     */
    private String createSheetNumber() {
        String sheetNumber = CommonUtil.createBusinessSerialNumber ("SP");
        Predicate predicate = QFlowSheet.flowSheet.sheetNumber.eq (sheetNumber);
        List<FlowSheet> flowSheets = Lists.newArrayList (flowSheetDao.findAll (predicate));
        if (flowSheets.size () > 0) {
            sheetNumber = createSheetNumber ();
        }
        return sheetNumber;
    }

    /**
     * 将申请数据中的附件移动到正式目录
     *
     * @param flowSheet 审批单对象
     */
    private void transplantAttach(FlowSheet flowSheet) {
        //流程应用ID
        String flowAppId = flowSheet.getFlowAppId ();
        WorkFlowApp workFlowApp = workFlowAppDao.findById (Long.valueOf (flowAppId)).orElse (null);
        //表单输入项列表
        List<FlowFormInput> inputs = workFlowApp.getInputs ();
        //申请数据
        List<SheetData> sheetDatas = flowSheet.getSheetData ();
        for (SheetData sheetData : sheetDatas) {
            for (FlowFormInput flowFormInput : inputs) {
                if (flowFormInput.getTitle ().equals (sheetData.getTitle ())
                        && flowFormInput.getInputType ().equals (sheetData.getInputType ())) {
                    if (flowFormInput.getIsBuildIn ()) {
                        sheetData.setCode (flowFormInput.getCode ());
                    }
                }
            }
            if (sheetData.getInputType () == FormInputType.Picture) {
                //将图片移到正式的bucket中
                sheetData.setValue (transplantPicture (sheetData.getValue ()));
            } else if (sheetData.getInputType () == FormInputType.File) {
                //将附件移到正式的bucket中
                sheetData.setValue (transplantFile (sheetData.getValue ()));
            }
        }
    }

    /**
     * 将图片移到正式的bucket中
     */
    private String transplantPicture(String value) {
        List<Object> newProfiles = CommonUtil.strTransformListObject (value);
        if (newProfiles == null) {
            return null;
        } else {
            List<String> stringList = new ArrayList<> ();
            for (Object obj : newProfiles) {
                String url = obj.toString ();
                String destPath = "company/audit/";
                stringList.add (fileService.copyAttachment (url, destPath, ""));
            }
            return CommonUtil.toJson (stringList);
        }
    }

    /**
     * 将文件移到正式的bucket中
     */
    private String transplantFile(String value) {
        List<Object> files = CommonUtil.strTransformListObject (value);
        if (files == null) {
            return null;
        } else {
            List<Map<String, Object>> maps = new ArrayList<> ();
            for (Object file : files) {
                Map<String, Object> uploadFiler = (Map) file;
                String url = uploadFiler.get ("uri").toString ();
                String name = uploadFiler.get ("name").toString ();
                String fileType = name.substring (name.lastIndexOf ("."));
                String destPath = "company/audit/";
                uploadFiler.put ("uri", fileService.copyAttachment (url, destPath, fileType));
                maps.add (uploadFiler);
            }
            return CommonUtil.toJson (maps);
        }
    }

    /**
     * 生成流程引擎实例
     *
     * @param flowSheet 审批单对象
     * @param isReApply 是否重新申请
     */
    private String crateProcessInstance(FlowSheet flowSheet, Boolean isReApply) {
        //流程应用ID
        String flowAppId = flowSheet.getFlowAppId ();
        WorkFlowApp workFlowApp = workFlowAppDao.findById (Long.valueOf (flowAppId)).orElse (null);
        if (workFlowApp == null) {
            throw new ServiceException (GenExceptCode.Request_Param);
        }
        //流程定义
        FlowDefinition flowDefinition = workFlowApp.getFlowDefinition ();
        if (flowDefinition == null) {
            throw new ServiceException ("FLOW_DEFINITION_NOT_BE_NULL", "请配置流程定义!");
        }
        if (flowDefinition.getIsFree ()) {
            List<FlowPerson> auditPersons = flowSheet.getAuditPersons ();
            if (auditPersons == null || auditPersons.isEmpty ()) {
                throw new ServiceException ("FREE_AUDITOR_NOT_BE_NULL", "自由流程审批人列表不能为空!");
            }
            flowSheetDao.save (flowSheet);
            // 如果是自由流程，则需要先创建流程引擎的流程定义
            String processKey = createFreeProcessDefinition (auditPersons, workFlowApp);
            // 创建自由流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey (processKey, flowSheet.getId ().toString ());
            flowSheet.setInstanceId (processInstance.getId ());
        } else {
            // 固定流程,
            flowSheetDao.save (flowSheet);
            //审批节点列表
            List<FlowNodeDef> nodeDefs = flowDefinition.getNodeDefs ();
            Map<String, Object> variables = getNodeVariables (nodeDefs);
            //流程引擎流程定义ID
            String actDefId = flowDefinition.getActDefId ();
            // 创建固定流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey (actDefId, flowSheet.getId ().toString (), variables);
            flowSheet.setInstanceId (processInstance.getId ());
        }
        // 记录日志
        String comment = isReApply ? "重新申请" : "发起申请";
        createAuditLog (flowSheet, isReApply ? AuditLogStatus.ReApply : AuditLogStatus.Apply,
                flowSheet.getStaffId (), flowSheet.getStaffName (), comment);
        // 设置状态
        flowSheet.setStatus (FlowSheetStatus.Approving);
        flowSheet.setIsFinished (false);
        flowSheetDao.save (flowSheet);

        // 发布审批流程申请事件
        List<Task> tasks = taskService.createTaskQuery ().processInstanceBusinessKey (flowSheet.getId ().toString ()).list ();
        List<String> staffIds = new ArrayList<> ();
        if (tasks != null && !tasks.isEmpty ()) {
            staffIds = tasks.stream ().map (Task::getAssignee).collect (Collectors.toList ());
        }
        Map<String, Object> eventMap = new HashMap<> ();
        eventMap.put ("sheetId", flowSheet.getId ());
        eventMap.put ("staffIds", staffIds);
        eventMap.put ("staffName", flowSheet.getStaffName ());
        eventMap.put ("appName", workFlowApp.getName ());
        String message = CommonUtil.buildEventMessage (EventTypes.COMPANY_WORKFLOW_APPLY, eventMap);
        redisTemplate.convertAndSend (EventTypes.TOPIC_COMPANY, message);

        return flowSheet.getId ().toString ();
    }

    /**
     * 创建自由流程的流程引擎的流程定义
     *
     * @param workFlowApp 流程应用
     */
    private String createFreeProcessDefinition(List<FlowPerson> auditPersons, WorkFlowApp workFlowApp) {
        Process process = new Process ();
        //流程应用id,此处加上a是因为uuid如果以数字开头，则process.setId会出错。
        String processKey = "a" + CommonUtil.getUUID ();
        process.setId (processKey);
        //流程应用名称
        process.setName (workFlowApp.getName () + "流程");
        //设置开始节点
        process.addFlowElement (ActivitiUtils.createStartEvent ());
        int taskId = 1;
        for (FlowPerson auditPerson : auditPersons) {
            //节点名称
            String name = auditPerson.getStaffName ();
            String taskKey = "task" + taskId;
            String assignee = auditPerson.getStaffId ();
            //设置任务节点
            process.addFlowElement (ActivitiUtils.createUserTask (taskKey, name, assignee));
            String createExclusiveGatewayName = "createExclusiveGateway" + taskId;
            //设置判断节点
            process.addFlowElement (ActivitiUtils.createExclusiveGateway (createExclusiveGatewayName));
            taskId++;
        }
        //设置结束节点
        process.addFlowElement (ActivitiUtils.createEndEvent ());
        //设置节点之间的连线
        if (taskId > 1) {
            process.addFlowElement (ActivitiUtils.createSequenceFlow ("start", "task1"));
        } else {
            process.addFlowElement (ActivitiUtils.createSequenceFlow ("start", "end"));
        }
        String conditionExpression1 = "${approved==false}";
        String conditionExpression2 = "${approved==true}";
        for (int i = 1; i < taskId; i++) {
            String gatewayVar = "createExclusiveGateway" + i;
            process.addFlowElement (ActivitiUtils.createSequenceFlow ("task" + i, gatewayVar));
            process.addFlowElement (ActivitiUtils.createSequenceFlow (gatewayVar, "end", "不通过", conditionExpression1));
            if (taskId == (i + 1)) {
                process.addFlowElement (ActivitiUtils.createSequenceFlow (gatewayVar, "end", "通过", conditionExpression2));
            } else {
                process.addFlowElement (ActivitiUtils.createSequenceFlow (gatewayVar, "task" + (i + 1), "通过", conditionExpression2));
            }
        }
        // 创建一个bpmnModel对象 添加process
        BpmnModel bpmnModel = new BpmnModel ();
        bpmnModel.addProcess (process);
        //部署流程
        repositoryService.createDeployment ()
                .addBpmnModel (processKey + ".bpmn", bpmnModel).name (workFlowApp.getName ())
                .deploy ();
        //流程定义
        FlowDefinition definition = workFlowApp.getFlowDefinition ();
        definition.setActDefId (processKey);
        flowDefinitionDao.save (definition);
        return processKey;
    }

    /**
     * 获取审批节点变量
     */
    private Map<String, Object> getNodeVariables(List<FlowNodeDef> nodeDefs) {
        Map<String, Object> objectMap = new HashMap<> ();
        if (nodeDefs != null && nodeDefs.size () > 0) {
            for (int i = 0; i < nodeDefs.size (); i++) {
                FlowNodeDef nodeDef = nodeDefs.get (i);
                //审批人类型
                AuditPersonType auditPersonType = nodeDef.getAuditPersonType ();
                //指定成员
                if (auditPersonType.equals (AuditPersonType.AssignPerson)) {
                    //成员列表
                    List<FlowPerson> auditPersons = nodeDef.getAuditPersons ();
                    List<String> auditIds = new ArrayList<> ();
                    if (auditPersons == null) {
                        auditPersons = new ArrayList<> ();
                    }
                    Iterator<FlowPerson> it = auditPersons.iterator ();
                    while (it.hasNext ()) {
                        FlowPerson audit = it.next ();
                        Boolean bool = findStaffService.findStaffIsExist (audit.getStaffId ());
                        if (!bool) {
                            it.remove ();
                        } else {
                            auditIds.add (audit.getStaffId ());
                        }
                    }
                    if (auditPersons.isEmpty ()) {
                        objectMap.put ("approved", true);
                    }
                    objectMap.put ("userList" + (i + 1), auditIds);
                } else if (auditPersonType.equals (AuditPersonType.AssignRole)) {
                    //指定角色
                    //角色列表
                    List<FlowPerson> auditPersons = nodeDef.getAuditPersons ();
                    List<String> staffIds = new ArrayList<> ();
                    for (FlowPerson audit : auditPersons) {
                        List<Map<String, Object>> list = findRolesService.findCompanyIsExist (audit.getStaffId ());
                        if (list == null) {
                            list = new ArrayList<> ();
                        }
                        for (Map<String, Object> map : list) {
                            staffIds.add (map.get ("id").toString ());
                        }
                    }
                    if (staffIds.isEmpty ()) {
                        objectMap.put ("approved", true);
                    }
                    objectMap.put ("userList" + (i + 1), staffIds);
                }
            }
        }
        return objectMap;
    }

    /**
     * 查询审批表单列表
     *
     * @param submitId   提交人id
     * @param auditorId  审批人id
     * @param queryType  查询类型
     * @param copyUserId 抄送人id
     * @param companyId  企业id
     * @param status     审批单状态
     * @param flowAppId  审批应用id
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param isFinished 是否已完成
     * @param order      排序条件
     * @return
     */
    public Page<FlowSheet> getApplicantSheets(String submitId, String auditorId, QueryType queryType, String copyUserId,
                                              String companyId, FlowSheetStatus status,
                                              String flowAppId, String startTime, String endTime,
                                              Boolean isFinished, Pageable pageable, String order) {
        BooleanExpression expression;
        if (StringUtils.isNotBlank (submitId)) {
            //提交人
            expression = QFlowSheet.flowSheet.staffId.eq (submitId);
        } else if (StringUtils.isNotBlank (auditorId)) {
            //审批人
            Set<Long> businessKeys = businessKeys (auditorId, queryType);
            expression = QFlowSheet.flowSheet.id.in (businessKeys);
        } else if (StringUtils.isNotBlank (copyUserId)) {
            //抄送人
            Page<FlowSheet> flowSheetPage = copyUserIdFind (copyUserId, companyId, status, flowAppId, startTime, endTime, isFinished, pageable, order);
            findCurrentNode (flowSheetPage.getContent ());
            return flowSheetPage;
        } else {
            throw new ServiceException (GenExceptCode.Request_Param);
        }
        if (StringUtils.isNotBlank (companyId)) {
            expression = expression.and (QFlowSheet.flowSheet.companyId.eq (companyId));
        }
        if (StringUtils.isNotBlank (startTime)) {
            expression = expression.and (QFlowSheet.flowSheet.submitTime.goe (DateFormatUtil.parseDateTime (startTime)));
        }
        if (StringUtils.isNotBlank (endTime)) {
            expression = expression.and (QFlowSheet.flowSheet.submitTime.lt (DateFormatUtil.parseDateTime (endTime)));
        }
        if (isFinished != null) {
            expression = expression.and (QFlowSheet.flowSheet.isFinished.eq (isFinished));
        }
        if (StringUtils.isNotBlank (flowAppId)) {
            expression = expression.and (QFlowSheet.flowSheet.flowAppId.eq (flowAppId));
        }
        if (status != null) {
            expression = expression.and (QFlowSheet.flowSheet.status.eq (status));
        }
        Page<FlowSheet> flowSheetPage = flowSheetDao.findAll (expression, pageable);
        findCurrentNode (flowSheetPage.getContent ());
        return flowSheetPage;
    }

    /**
     * 查询流程实例当前节点
     *
     * @return
     */
    private List<FlowSheet> findCurrentNode(List<FlowSheet> flowSheetList) {
        for (FlowSheet flowSheet : flowSheetList) {
            //流程应用ID
            String flowAppId = flowSheet.getFlowAppId ();
            //流程引擎流程实例ID
            String instanceId = flowSheet.getInstanceId ();
            List<Task> tasks = taskService.createTaskQuery ().processInstanceId (instanceId).active ().orderByTaskCreateTime ().desc ().list ();
            if (!tasks.isEmpty ()) {
                FlowCurrentNode flowCurrentNode = new FlowCurrentNode ();
                String name = tasks.get (0).getName ();
                WorkFlowApp workFlowApp = workFlowAppDao.findById (Long.valueOf (flowAppId)).orElse (null);
                if (workFlowApp != null) {
                    FlowDefinition flowDefinition = workFlowApp.getFlowDefinition ();
                    if (flowDefinition != null) {
                        //是否自由流程
                        Boolean isFree = flowDefinition.getIsFree ();
                        if (isFree) {
                            flowCurrentNode.setAuditType (null);
                        } else {
                            List<FlowNodeDef> nodeDefs = flowDefinition.getNodeDefs ();
                            for (FlowNodeDef flowNodeDef : nodeDefs) {
                                if (flowNodeDef.getName ().equals (name)) {
                                    flowCurrentNode.setAuditType (flowNodeDef.getAuditType ());
                                    break;
                                }
                            }
                        }
                        flowCurrentNode.setName (name);
                        List<String> todoPersons = new ArrayList<> ();
                        for (Task task : tasks) {
                            todoPersons.add (task.getAssignee ());
                        }
                        flowCurrentNode.setTodoPersons (todoPersons);
                        flowSheet.setCurrentNode (flowCurrentNode);
                    }
                }
            }
        }
        return flowSheetList;

    }

    /**
     * 根据抄送人查询
     *
     * @param copyUserId
     * @param companyId
     * @param status
     * @param flowAppId
     * @param startTime
     * @param endTime
     * @param isFinished
     * @param pageable
     * @param order
     * @return
     */
    private Page<FlowSheet> copyUserIdFind(String copyUserId, String companyId, FlowSheetStatus status, String flowAppId,
                                           String startTime, String endTime, Boolean isFinished, Pageable pageable, String order) {
        //抄送人
        EntityManager em = entityManagerFactory.getNativeEntityManagerFactory ().createEntityManager ();
        StringBuilder whereSql = new StringBuilder ();
        whereSql.append ("and copy_persons @> '[{\"staffId\":\"").append (copyUserId).append ("\"}]'");
        if (StringUtils.isNotBlank (companyId)) {
            whereSql.append (" and company_id='").append (companyId).append ("'");
        }
        if (StringUtils.isNotBlank (flowAppId)) {
            whereSql.append (" and flow_app_id='").append (flowAppId).append ("'");
        }
        if (StringUtils.isNotBlank (startTime)) {
            whereSql.append (" and submit_time>'").append (startTime).append ("'");
        }
        if (StringUtils.isNotBlank (endTime)) {
            whereSql.append (" and submit_time<'").append (endTime).append ("'");
        }
        if (isFinished != null) {
            whereSql.append (" and is_finished='").append (status).append ("'");
        }
        if (status != null) {
            whereSql.append (" and status='").append (isFinished).append ("'");
        }
        em.getTransaction ().begin ();
        String countSql = "select count(*) from flow_sheet where 1=1 " + whereSql.toString ();
        Query countQuery = em.createNativeQuery (countSql);
        BigInteger count = (BigInteger) countQuery.getSingleResult ();
        if (StringUtils.isNotBlank (order)) {
            whereSql.append (" order by ");
            if (order.contains (",")) {
                String[] o = order.split (",");
                for (String or : o) {
                    whereSql.append (humpToLine (or.split ("\\|")[0])).append (" ").append (or.split ("\\|")[1]);
                    whereSql.append (",");
                }
                whereSql.substring (0, whereSql.length () - 1);
            } else {
                whereSql.append (humpToLine (order.split ("\\|")[0])).append (" ").append (order.split ("\\|")[1]);
            }
        }
        String querySql = "select * from  flow_sheet where 1=1 " + whereSql.toString ();
        Query query = em.createNativeQuery (querySql, FlowSheet.class)
                .setFirstResult ((int) pageable.getOffset ()).setMaxResults (pageable.getPageSize ());
        return new PageImpl<FlowSheet> (query.getResultList (), pageable, count.longValue ());
    }

    /**
     * 驼峰转下划线
     */
    private String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher (str);
        StringBuffer sb = new StringBuffer ();
        while (matcher.find ()) {
            matcher.appendReplacement (sb, "_" + matcher.group (0).toLowerCase ());
        }
        matcher.appendTail (sb);
        return sb.toString ();
    }

    /**
     * 根据审批人id查询审批单id
     *
     * @param auditorId
     * @param queryType
     * @return
     */
    private Set<Long> businessKeys(String auditorId, QueryType queryType) {
        //businessKeyId
        Set<Long> businessKeys = new HashSet<> ();
        if (QueryType.Already.equals (queryType)) {
            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery ()
                    .taskAssignee (auditorId).finished ().list ();
            for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
                //流程实例ID
                HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery ()
                        .processInstanceId (historicTaskInstance.getProcessInstanceId ());
                for (HistoricProcessInstance historicProcessInstance : query.list ()) {
                    businessKeys.add (Long.valueOf (historicProcessInstance.getBusinessKey ()));
                }
            }
        } else if (QueryType.Pending.equals (queryType)) {
            TaskQuery taskQuery = taskService.createTaskQuery ().taskAssignee (auditorId);
            List<Task> tasks = taskQuery.list ();
            for (Task task : tasks) {
                String processInstanceId = task.getProcessInstanceId ();
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery ()
                        .processInstanceId (processInstanceId).active ().singleResult ();
                if (processInstance == null) {
                    continue;
                }
                String businessKey = processInstance.getBusinessKey ();
                if (businessKey == null) {
                    continue;
                }
                businessKeys.add (Long.valueOf (businessKey));
            }
        } else {
            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery ()
                    .taskAssignee (auditorId).finished ().list ();
            for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
                //流程实例ID
                HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery ()
                        .processInstanceId (historicTaskInstance.getProcessInstanceId ());
                for (HistoricProcessInstance historicProcessInstance : query.list ()) {
                    businessKeys.add (Long.valueOf (historicProcessInstance.getBusinessKey ()));
                }
            }
            TaskQuery taskQuery = taskService.createTaskQuery ().taskAssignee (auditorId);
            List<Task> tasks = taskQuery.list ();
            for (Task task : tasks) {
                String processInstanceId = task.getProcessInstanceId ();
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery ()
                        .processInstanceId (processInstanceId).active ().singleResult ();
                if (processInstance == null) {
                    continue;
                }
                String businessKey = processInstance.getBusinessKey ();
                if (businessKey == null) {
                    continue;
                }
                businessKeys.add (Long.valueOf (businessKey));
            }
        }

        return businessKeys;
    }

    /**
     * 根据id查询审批单详情
     *
     * @param id 审批单id
     * @return 返回审批单对象
     */
    public FlowSheet getFlowSheetById(Long id) {
        Assert.hasText (id.toString (), GenExceptCode.Request_Param.getMsg ());
        List<FlowSheet> flowSheetList = new ArrayList<> ();
        FlowSheet flowSheet = flowSheetDao.findById (id).orElse (null);
        flowSheetList.add (flowSheet);
        findCurrentNode (flowSheetList);
        return findCurrentNode (flowSheetList).get (0);
    }

    /**
     * 审批通过
     *
     * @param id        审批单id
     * @param auditorId 审批人id
     * @param comment   审批意见
     */
    public void approve(Long id, String auditorId, String auditorName, String comment) {
        FlowSheet flowSheet = handleTask (id, auditorId, auditorName, comment, true);
        WorkFlowApp workFlowApp = workFlowAppDao.findById (Long.valueOf (flowSheet.getFlowAppId ())).orElse (null);
        if (workFlowApp == null) {
            throw new ServiceException (GenExceptCode.Request_Param.name (), WorkFlowApp.class.getSimpleName ());
        }
        List<Task> tasks = taskService.createTaskQuery ().processInstanceBusinessKey (id.toString ()).list ();
        if (tasks == null || tasks.isEmpty ()) {
            // 如果此审批单下的代办任务为空，则表示流程已走完
            //任务完成
            flowSheet.setIsFinished (true);
            //修改任务状态为审批通过
            flowSheet.setStatus (FlowSheetStatus.Approved);
            // 全部节点审批通过，发布审批同意事件
            Map<String, Object> eventMap = new HashMap<> ();
            eventMap.put ("sheetId", flowSheet.getId ());
            eventMap.put ("companyId", flowSheet.getCompanyId ());
            eventMap.put ("staffId", flowSheet.getStaffId ());
            eventMap.put ("staffName", flowSheet.getStaffName ());
            eventMap.put ("appName", workFlowApp.getName ());
            if (FlowAppType.Leave.equals (workFlowApp.getAppType ()) ||
                    FlowAppType.Card.equals (workFlowApp.getAppType ())) {
                eventMap.put ("submitTime", flowSheet.getSubmitTime ());
                eventMap.put ("appType", workFlowApp.getAppType ().name ());
                eventMap.put ("sheetData", flowSheet.getSheetData ());
            }
            String message = CommonUtil.buildEventMessage (EventTypes.COMPANY_WORKFLOW_APPROVE, eventMap);
            redisTemplate.convertAndSend (EventTypes.TOPIC_COMPANY, message);

            // 发布审批抄送事件
            Map<String, Object> copyEventMap = new HashMap<> ();
            List<FlowPerson> copyPersons = flowSheet.getCopyPersons ();
            if (copyPersons != null && !copyPersons.isEmpty ()) {
                copyEventMap.put ("sheetId", flowSheet.getId ());
                copyEventMap.put ("staffIds", copyPersons.stream ().map (FlowPerson::getStaffId).collect (Collectors.toList ()));
                copyEventMap.put ("staffName", flowSheet.getStaffName ());
                copyEventMap.put ("appName", workFlowApp.getName ());
                String copyMessage = CommonUtil.buildEventMessage (EventTypes.COMPANY_WORKFLOW_COPY, copyEventMap);
                redisTemplate.convertAndSend (EventTypes.TOPIC_COMPANY, copyMessage);
            }
        } else {
            // 进入下一个节点，发布审批流程申请事件
            List<String> staffIds = tasks.stream ().map (Task::getAssignee).collect (Collectors.toList ());
            Map<String, Object> eventMap = new HashMap<> ();
            eventMap.put ("sheetId", flowSheet.getId ());
            eventMap.put ("staffIds", staffIds);
            eventMap.put ("staffName", flowSheet.getStaffName ());
            eventMap.put ("appName", workFlowApp.getName ());
            String message = CommonUtil.buildEventMessage (EventTypes.COMPANY_WORKFLOW_APPLY, eventMap);
            redisTemplate.convertAndSend (EventTypes.TOPIC_COMPANY, message);
        }
        flowSheetDao.save (flowSheet);
    }

    /**
     * 审批拒绝
     *
     * @param id        审批单id
     * @param auditorId 审批人id
     * @param comment   审批意见
     */
    public void reject(Long id, String auditorId, String auditorName, String comment) {
        FlowSheet flowSheet = handleTask (id, auditorId, auditorName, comment, false);
        WorkFlowApp workFlowApp = workFlowAppDao.findById (Long.valueOf (flowSheet.getFlowAppId ())).orElse (null);
        if (workFlowApp == null) {
            throw new ServiceException (GenExceptCode.Request_Param.name (), WorkFlowApp.class.getSimpleName ());
        }
        //审批拒绝则任务完成
        flowSheet.setIsFinished (true);
        //修改任务状态为审批拒绝
        flowSheet.setStatus (FlowSheetStatus.Reject);
        flowSheetDao.save (flowSheet);

        // 发布审批拒绝事件
        Map<String, Object> eventMap = new HashMap<> ();
        eventMap.put ("sheetId", flowSheet.getId ());
        eventMap.put ("staffId", flowSheet.getStaffId ());
        eventMap.put ("staffName", flowSheet.getStaffName ());
        eventMap.put ("auditorName", auditorName);
        eventMap.put ("appName", workFlowApp.getName ());
        String message = CommonUtil.buildEventMessage (EventTypes.COMPANY_WORKFLOW_REJECT, eventMap);
        redisTemplate.convertAndSend (EventTypes.TOPIC_COMPANY, message);
    }

    /**
     * 任务处理
     *
     * @param id         审批单id
     * @param auditorId  审批人id
     * @param comment    审批意见
     * @param isApproved 是否通过
     * @return
     */
    private FlowSheet handleTask(Long id, String auditorId, String auditorName, String comment, Boolean isApproved) {
        FlowSheet flowSheet = getFlowSheetById (id);
        if (flowSheet == null) {
            throw new ServiceException (GenExceptCode.Request_Param);
        }
        WorkFlowApp workFlowApp = workFlowAppDao.findById (Long.valueOf (flowSheet.getFlowAppId ())).orElse (null);
        if (workFlowApp == null) {
            throw new ServiceException (GenExceptCode.Request_Param.name (), WorkFlowApp.class.getSimpleName ());
        }
        FlowDefinition definition = workFlowApp.getFlowDefinition ();
        List<FlowNodeDef> nodes = definition.getNodeDefs ();
        // 获取当前审批单中此审批人的代办任务
        Task task = taskService.createTaskQuery ().processInstanceBusinessKey (id.toString ()).taskAssignee (auditorId).singleResult ();
        if (task == null) {
            throw new ServiceException ("TASK_NOT_FOUND", "没有找到待办任务!");
        }
        Map<String, Object> variables = new HashMap<> ();
        variables.put ("approved", isApproved);
        // 完成任务
        taskService.complete (task.getId (), variables);
        if (!definition.getIsFree () && isApproved) {
            // 固定流程抄送处理
            List<Task> unCompleteTasks = taskService.createTaskQuery ().processInstanceBusinessKey (id.toString ()).taskName (task.getName ()).list ();
            // 如果根据任务名称查询不到任务，说明此任务节点已经完成，则进行抄送
            if (unCompleteTasks == null || unCompleteTasks.isEmpty ()) {
                // 查询此节点是否有抄送人列表
                List<FlowPerson> copyPersons = new ArrayList<> ();
                for (FlowNodeDef node : nodes) {
                    if (node.getName ().equals (task.getName ())) {
                        copyPersons = node.getCopyPersons ();
                        break;
                    }
                }
                if (copyPersons != null && !copyPersons.isEmpty ()) {
                    List<FlowPerson> cps = flowSheet.getCopyPersons ();
                    if (cps == null) {
                        cps = new ArrayList<> ();
                    }
                    // 如果有抄送人，则将抄送人列表叠加到审批单审批列表
                    cps.addAll (copyPersons);
                    // 利用set集合唯一性去重
                    Set<FlowPerson> set = new HashSet<> (cps);
                    List<FlowPerson> tempList = new ArrayList<> (set);
                    flowSheet.setCopyPersons (tempList);
                }
            }
        }
        // 记录日志
        createAuditLog (flowSheet, isApproved ? AuditLogStatus.Approved : AuditLogStatus.Rejected, auditorId, auditorName, comment);
        return flowSheet;
    }

    /**
     * 撤销申请
     *
     * @param id 审批单id
     */
    public void cancel(Long id) {
        FlowSheet flowSheet = getFlowSheetById (id);
        if (flowSheet == null) {
            throw new ServiceException (GenExceptCode.Request_Param);
        }
        String comment = "撤销申请";
        runtimeService.deleteProcessInstance (flowSheet.getInstanceId (), comment);
        // 记录日志
        createAuditLog (flowSheet, AuditLogStatus.Canceled, flowSheet.getStaffId (), flowSheet.getStaffName (), comment);
        // 设置状态
        flowSheet.setIsFinished (true);
        flowSheet.setStatus (FlowSheetStatus.Cancel);
        flowSheetDao.save (flowSheet);
    }

    /**
     * 重新申请
     *
     * @param id      审批单id
     * @param sheetBO 重新申请审批单对象数据
     */
    public void reApply(Long id, FlowSheetBO sheetBO) {
        FlowSheet flowSheet = getFlowSheetById (id);
        if (flowSheet == null) {
            throw new ServiceException (GenExceptCode.Request_Param);
        }
        CopyUtil.copyNotNullProperties (sheetBO, flowSheet);
        transplantAttach (flowSheet);
        //生成流程引擎实例
        crateProcessInstance (flowSheet, true);
    }

    /**
     * 生成审批日志
     *
     * @param flowSheet   审批单对象
     * @param status      日志状态
     * @param auditorId   审批人id
     * @param auditorName 审批人名称
     * @param comment     审批意见
     */
    private void createAuditLog(FlowSheet flowSheet, AuditLogStatus status, String auditorId, String auditorName, String comment) {
        AuditLog log = new AuditLog ();
        log.setStatus (status);
        log.setAuditorId (auditorId);
        log.setAuditorName (auditorName);
        log.setAuditTime (LocalDateTime.now ());
        log.setComment (comment);
        List<AuditLog> auditLogs = flowSheet.getAuditLogs ();
        if (auditLogs == null) {
            auditLogs = new ArrayList<> ();
        }
        auditLogs.add (log);
        flowSheet.setAuditLogs (auditLogs);
    }
}
