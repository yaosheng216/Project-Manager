package com.eying.pcss.workflow.service;

import com.eying.pcss.core.exception.GenExceptCode;
import com.eying.pcss.core.exception.ServiceException;
import com.eying.pcss.core.util.CommonUtil;
import com.eying.pcss.core.util.CopyUtil;
import com.eying.pcss.workflow.bo.FlowDefinitionBO;
import com.eying.pcss.workflow.bo.WorkFlowAppBO;
import com.eying.pcss.workflow.constant.AuditType;
import com.eying.pcss.workflow.dao.FlowDefinitionDao;
import com.eying.pcss.workflow.dao.FlowSheetDao;
import com.eying.pcss.workflow.dao.WorkFlowAppDao;
import com.eying.pcss.workflow.dto.FlowFormInput;
import com.eying.pcss.workflow.dto.FlowNodeDef;
import com.eying.pcss.workflow.entity.*;
import com.eying.pcss.workflow.entity.QFlowSheet;
import com.eying.pcss.workflow.entity.QWorkFlowApp;
import com.eying.pcss.workflow.util.ActivitiUtils;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.jsonwebtoken.lang.Assert;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 审批流程应用Service类
 */
@Service
public class WorkFlowAppService {

    private final WorkFlowAppDao workFlowAppDao;
    private final FlowDefinitionDao flowDefinitionDao;
    private final FlowSheetDao flowSheetDao;
    private final FlowGroupService groupService;
    private final RepositoryService repositoryService;

    @Autowired
    public WorkFlowAppService(WorkFlowAppDao workFlowAppDao, FlowDefinitionDao flowDefinitionDao,
                              FlowSheetDao flowSheetDao, FlowGroupService groupService,
                              RepositoryService repositoryService) {
        this.workFlowAppDao = workFlowAppDao;
        this.flowDefinitionDao = flowDefinitionDao;
        this.flowSheetDao = flowSheetDao;
        this.groupService = groupService;
        this.repositoryService = repositoryService;
    }

    /**
     * 根据企业id和分组名称查询流程定义
     *
     * @param companyId
     * @param name
     * @return
     */
    public List<WorkFlowApp> getApps(String companyId, String name, Long groupId) {
        BooleanExpression expression = null;
        if (StringUtils.isNotBlank (companyId)) {
            expression = QWorkFlowApp.workFlowApp.companyId.eq (companyId);
        }
        if (StringUtils.isNotBlank (name)) {
            if (expression == null) {
                expression = QWorkFlowApp.workFlowApp.name.eq (name);
            } else {
                expression = expression.and (QWorkFlowApp.workFlowApp.name.eq (name));
            }
        }
        if (StringUtils.isNotBlank (groupId.toString ())) {
            if (expression == null) {
                expression = QWorkFlowApp.workFlowApp.group.id.eq (groupId);
            } else {
                expression = expression.and (QWorkFlowApp.workFlowApp.group.id.eq (groupId));
            }
        }
        if (expression == null) {
            return workFlowAppDao.findAll ();
        }
        return Lists.newArrayList (workFlowAppDao.findAll (expression));
    }

    /**
     * 暂存审批流程应用
     *
     * @param workFlowAppBO
     * @return
     */
    public String createGroupApp(WorkFlowAppBO workFlowAppBO) {
        WorkFlowApp workFlowApp;
        if (StringUtils.isNotBlank (workFlowAppBO.getId () == null ? null : workFlowAppBO.getId ().toString ())) {
            workFlowApp = workFlowAppDao.findById (workFlowAppBO.getId ()).orElse (null);
            if (workFlowApp == null) {
                throw new ServiceException (GenExceptCode.Request_Param);
            }
        } else {
            workFlowApp = new WorkFlowApp ();
        }
        CopyUtil.copyNotNullProperties (workFlowAppBO, workFlowApp);
        if (StringUtils.isNotBlank (workFlowAppBO.getGroupId ())) {
            FlowGroup group = groupService.getFlowGroup (Long.valueOf (workFlowAppBO.getGroupId ()));
            if (group == null) {
                throw new ServiceException (GenExceptCode.Request_Param.name (), FlowGroup.class.getSimpleName ());
            }
            workFlowApp.setGroup (group);
        }
        if (workFlowAppBO.getFlow () != null) {
            FlowDefinition flowDef = workFlowApp.getFlowDefinition ();
            if (flowDef == null) {
                flowDef = new FlowDefinition ();
            }
            CopyUtil.copyNotNullProperties (workFlowAppBO.getFlow (), flowDef);
            flowDefinitionDao.save (flowDef);
            workFlowApp.setFlowDefinition (flowDef);
        }
        return workFlowAppDao.save (workFlowApp).getId ().toString ();
    }

    /**
     * 根据id查询应用
     *
     * @param id
     * @return
     */
    public WorkFlowApp getApp(Long id) {
        return workFlowAppDao.findById (id).orElse (null);
    }

    /**
     * 编辑应用
     *
     * @param id
     * @param app
     */
    public void editApp(Long id, WorkFlowAppBO app) {
        WorkFlowApp workFlowApp = workFlowAppDao.findById (id).orElse (null);
        if (workFlowApp == null) {
            throw new ServiceException (GenExceptCode.Request_Param);
        }
        CopyUtil.copyNotNullProperties (app, workFlowApp);
        if (StringUtils.isNotBlank (app.getGroupId ())) {
            FlowGroup group = groupService.getFlowGroup (Long.valueOf (app.getGroupId ()));
            if (group == null) {
                throw new ServiceException (GenExceptCode.Request_Param.name (), FlowGroup.class.getSimpleName ());
            }
            workFlowApp.setGroup (group);
        }
        if (app.getFlow () != null) {
            FlowDefinition flowDef = workFlowApp.getFlowDefinition ();
            if (flowDef == null) {
                flowDef = new FlowDefinition ();
                flowDef.setCompanyId (workFlowApp.getCompanyId ());
            }
            CopyUtil.copyNotNullProperties (app.getFlow (), flowDef);
            flowDefinitionDao.save (flowDef);
            workFlowApp.setFlowDefinition (flowDef);
        }
        workFlowAppDao.save (workFlowApp);
    }

    /**
     * 删除应用
     *
     * @param id
     */
    public void deleteApp(Long id) {
        WorkFlowApp workFlowApp = workFlowAppDao.findById (id).orElse (null);
        if (workFlowApp == null) {
            throw new ServiceException (GenExceptCode.Request_Param);
        }
        // 删除应用相关所有的审批单
        Predicate predicate = QFlowSheet.flowSheet.flowAppId.eq (id.toString ());
        List<FlowSheet> flowSheets = Lists.newArrayList (flowSheetDao.findAll (predicate));
        if (flowSheets.size () > 0) {
            flowSheetDao.deleteAll (flowSheets);
        }
        workFlowAppDao.deleteById (id);
        // 删除流程定义
        FlowDefinition fd = workFlowApp.getFlowDefinition ();
        if (fd != null) {
            flowDefinitionDao.delete (fd);
        }
    }

    /**
     * 停用应用
     *
     * @param id
     */
    public void disableApp(Long id) {
        WorkFlowApp workFlowApp = workFlowAppDao.findById (id).orElse (null);
        if (workFlowApp == null) {
            throw new ServiceException (GenExceptCode.Request_Param);
        }
        workFlowApp.setIsEnabled (false);
        workFlowAppDao.save (workFlowApp);
    }

    /**
     * 启用应用
     *
     * @param id
     */
    public void enableApp(Long id) {
        WorkFlowApp workFlowApp = workFlowAppDao.findById (id).orElse (null);
        if (workFlowApp == null) {
            throw new ServiceException (GenExceptCode.Request_Param);
        }
        workFlowApp.setIsEnabled (true);
        workFlowAppDao.save (workFlowApp);
    }

    /**
     * 编辑应用表单
     *
     * @param id
     * @param inputs
     */
    public void editAppForm(Long id, List<FlowFormInput> inputs) {
        WorkFlowApp workFlowApp = workFlowAppDao.findById (id).orElse (null);
        if (workFlowApp == null) {
            throw new ServiceException (GenExceptCode.Request_Param);
        }
        workFlowApp.setInputs (inputs);
        workFlowAppDao.save (workFlowApp);
    }

    /**
     * 发布应用
     *
     * @param id
     */
    public String publish(Long id) {
        Assert.hasText (id.toString (), GenExceptCode.Request_Param.getMsg ());
        WorkFlowApp workFlowApp = workFlowAppDao.findById (id).orElse (null);
        if (workFlowApp == null) {
            throw new ServiceException (GenExceptCode.Request_Param.name (), WorkFlowApp.class.getSimpleName ());
        }
        //校验企业id
        if (StringUtils.isBlank (workFlowApp.getCompanyId ())) {
            throw new ServiceException (GenExceptCode.Request_Param.name (), "companyId");
        }
        //校验应用名称
        if (StringUtils.isBlank (workFlowApp.getName ())) {
            throw new ServiceException (GenExceptCode.Request_Param.name (), "name");
        }
        //校验应用分组
        if (StringUtils.isNotBlank (workFlowApp.getGroup ().getId ().toString ())) {
            FlowGroup group = groupService.getFlowGroup (workFlowApp.getGroup ().getId ());
            if (group == null) {
                throw new ServiceException (GenExceptCode.Request_Param.name (), FlowGroup.class.getSimpleName ());
            }
        }
        //校验表单输入项列表
        List<FlowFormInput> inputs = workFlowApp.getInputs ();
        if (inputs == null || inputs.isEmpty ()) {
            throw new ServiceException (GenExceptCode.Request_Param.name (), "inputs");
        } else {
            for (FlowFormInput flowFormInput : inputs) {
                //校验输入类型
                if (flowFormInput.getInputType () == null) {
                    throw new ServiceException (GenExceptCode.Request_Param.name (), "FlowFormInput.inputType");
                }
                //校验显示标题
                if (StringUtils.isBlank (flowFormInput.getTitle ())) {
                    throw new ServiceException (GenExceptCode.Request_Param.name (), "FlowFormInput.title");
                }
            }
        }
        //校验审批流程定义
        FlowDefinition flowDefinition = workFlowApp.getFlowDefinition ();
        //校验企业id
        if (StringUtils.isBlank (flowDefinition.getCompanyId ())) {
            throw new ServiceException (GenExceptCode.Request_Param.name (), "companyId");
        }
        //是否自由流程
        if (!flowDefinition.getIsFree ()) {
            List<FlowNodeDef> nodeDefs = flowDefinition.getNodeDefs ();
            for (FlowNodeDef flowNodeDef : nodeDefs) {
                //校验审批类型
                if (flowNodeDef.getAuditType () == null) {
                    throw new ServiceException (GenExceptCode.Request_Param.name (), "FlowNodeDef.auditType");
                }
                //校验节点名称
                if (StringUtils.isBlank (flowNodeDef.getName ())) {
                    throw new ServiceException (GenExceptCode.Request_Param.name (), "FlowNodeDef.name");
                }
                //校验审批人类型
                if (flowNodeDef.getAuditPersonType () == null) {
                    throw new ServiceException (GenExceptCode.Request_Param.name (), "FlowNodeDef.auditPersonType");
                }
                //校验审批人列表
                if (flowNodeDef.getAuditPersons () == null) {
                    throw new ServiceException (GenExceptCode.Request_Param.name (), "FlowNodeDef.auditPersons");
                }
            }
        }

        // 根据流程应用中的流程节点生成流程引擎流程定义并进行流程部署
        deployActDefinition (workFlowApp);
        // 保存完整的流程应用
        workFlowAppDao.save (workFlowApp);
        return workFlowApp.getId ().toString ();
    }

    /**
     * 生成流程引擎流程定义并进行流程部署
     *
     * @param workFlowApp 流程应用对象
     */
    private void deployActDefinition(WorkFlowApp workFlowApp) {
        FlowDefinition definition = workFlowApp.getFlowDefinition ();
        if (definition == null) {
            throw new ServiceException ("FLOW_DEFINITION_NOT_BE_NULL", "请配置流程定义!");
        }
        if (!definition.getIsFree ()) {
            // 如果是固定流程才在发布的时候创建流程引擎流程定义
            List<FlowNodeDef> nodeDefs = definition.getNodeDefs ();
            Process process = new Process ();
            //流程应用id,此处加上a是因为uuid如果以数字开头，则process.setId会出错。
            String processKey = "a" + CommonUtil.getUUID ();
            process.setId (processKey);
            //流程应用名称
            process.setName (workFlowApp.getName () + "流程");
            //设置开始节点
            process.addFlowElement (ActivitiUtils.createStartEvent ());
            int taskId = 1;
            taskId = createActUserTasks (process, taskId, nodeDefs);
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

            // 将bpmnModel 转化为xml 测试展示用，后期删除******/
            BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter ();
            byte[] convertToXML = bpmnXMLConverter.convertToXML (bpmnModel);
            String bytes = new String (convertToXML);
            System.out.println (bytes);
            try {
                FileUtils.writeByteArrayToFile (new File ("D:/" + workFlowApp.getName () + ".bpmn"), convertToXML);
            } catch (IOException e) {
                e.printStackTrace ();
            }

            //部署流程
            repositoryService.createDeployment ()
                    .addBpmnModel (processKey + ".bpmn", bpmnModel).name (workFlowApp.getName ())
                    .deploy ();
            definition.setActDefId (processKey);
            flowDefinitionDao.save (definition);
        }
    }

    /**
     * 根据流程定义节点生成流程引擎节点任务
     *
     * @param process
     * @param taskId
     * @param nodeDefs
     */
    private int createActUserTasks(Process process, int taskId, List<FlowNodeDef> nodeDefs) {
        for (FlowNodeDef flowNodeDef : nodeDefs) {
            //审批类型
            AuditType auditType = flowNodeDef.getAuditType ();
            //节点名称
            String name = flowNodeDef.getName ();
            String taskKey = "task" + taskId;
            String assigneeVar = "user" + taskId;
            String inputDataItem = "userList" + taskId;
            String completionCondition = "";
            if (auditType.equals (AuditType.And)) {
                //会签
                completionCondition = "${nrOfCompletedInstances/nrOfInstances == 1}";
            } else if (auditType.equals (AuditType.Or)) {
                //或签
                completionCondition = "${nrOfCompletedInstances == 1}";
            }
            //设置任务节点
            process.addFlowElement (ActivitiUtils.createUserTask4MultiInstance (taskKey, name, assigneeVar, inputDataItem, completionCondition));
            String createExclusiveGatewayName = "createExclusiveGateway" + taskId;
            //设置判断节点
            process.addFlowElement (ActivitiUtils.createExclusiveGateway (createExclusiveGatewayName));
            taskId++;
        }
        return taskId;
    }

    /**
     * 创建流程定义
     *
     * @param flowDefinition
     */
    public void editAppFlow(Long id, FlowDefinitionBO flowDefinition) {
        WorkFlowApp workFlowApp = workFlowAppDao.findById (id).orElse (null);
        if (workFlowApp == null) {
            throw new ServiceException (GenExceptCode.Request_Param);
        }
        FlowDefinition flowDef = new FlowDefinition ();
        CopyUtil.copyNotNullProperties (flowDefinition, flowDef);
        flowDef.setCompanyId (workFlowApp.getCompanyId ());
        flowDefinitionDao.save (flowDef);
        workFlowApp.setFlowDefinition (flowDef);
        workFlowAppDao.save (workFlowApp);
    }


}
