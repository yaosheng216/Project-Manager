package com.eying.pcss.workflow.service;

import com.eying.pcss.workflow.entity.FlowGroup;
import com.eying.pcss.workflow.entity.FlowSheet;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.TaskQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.task.Task;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * FlowGroupService Tester.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FlowGroupServiceTest {
    @Autowired
    private FlowGroupService flowGroupService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ProcessEngine processEngine;

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    /**
     * Method: saveFlowGroup(String name, String companyId)
     */
    @Test
    public void testSaveFlowGroup() {
        flowGroupService.saveFlowGroup ("分组一", "123456");
    }

    /**
     * Method: findAll(String companyId)
     */
    @Test
    public void testFindAll() {
        List<FlowGroup> groups = flowGroupService.findAll ("123456");
        assertThat (groups.size ()).isGreaterThan (0);
    }

    //查询待办任务
    @Test
    public void A() {
        FlowSheet flowSheet = new FlowSheet ();
        TaskQuery taskQuery = taskService.createTaskQuery ().taskAssignee ("5ca5a04d52faff0001a93130");
        List<Task> tasks = taskQuery.list ();

        // 根据流程的业务ID查询实体并关联
        for (Task task : tasks) {
            String processInstanceId = task.getProcessInstanceId ();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery ().processInstanceId (processInstanceId).active ().singleResult ();
            if (processInstance == null) {
                continue;
            }
            String businessKey = processInstance.getBusinessKey ();
            if (businessKey == null) {
                continue;
            }
//            Leave leave = leaveManager.getLeave(new Long(businessKey));
//            leave.setTask(task);
//            leave.setProcessInstance(processInstance);
//            leave.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
        }
        //    System.out.println("assigneeList:" + assigneeList.toString());
    }

    //查询历史
    @Test
    public void hist() {

        List<HistoricTaskInstance> list = processEngine.getHistoryService () // 历史任务Service
                .createHistoricTaskInstanceQuery () // 创建历史任务实例查询
                .taskAssignee ("5cadbac8cff47e0001c904c8") // 指定办理人
                .list ();
        //流程实例ID
        List<String> processInstanceIdList = new ArrayList<> ();
        for (HistoricTaskInstance hti : list) {
            processInstanceIdList.add (hti.getProcessInstanceId ());
            System.out.println ("任务ID:" + hti.getId ());
            System.out.println ("流程实例ID:" + hti.getProcessInstanceId ());
            System.out.println ("办理人：" + hti.getAssignee ());
            System.out.println ("创建时间：" + hti.getCreateTime ());
            System.out.println ("结束时间：" + hti.getEndTime ());
            System.out.println ("===========================");
        }
        List<HistoricProcessInstance> list1 = new ArrayList<> ();
        for (String processInstanceId : processInstanceIdList) {
            HistoricProcessInstanceQuery query =
                    historyService.createHistoricProcessInstanceQuery ().processInstanceId (processInstanceId);
//                            .orderByProcessInstanceEndTime()
//                            .desc();
            for (HistoricProcessInstance historicProcessInstance : query.list ()) {
                list1.add (historicProcessInstance);
            }
        }
        System.out.println ("list===" + list1.toString ());
        //流程实例ID
        Set<String> businessKeys = new HashSet<> ();
        // 关联业务实体
        for (HistoricProcessInstance historicProcessInstance : list1) {
            String businessKey = historicProcessInstance.getBusinessKey ();
            businessKeys.add (businessKey);
            System.out.println ("业务key===" + businessKey);
//            System.out.println("流程定义id==="+historicProcessInstance.getProcessDefinitionId());
//            System.out.println("开始时间==="+historicProcessInstance.getStartTime());
//            System.out.println("结束时间==="+historicProcessInstance.getEndTime());
//            System.out.println("时长==="+historicProcessInstance.getDurationInMillis());
//            System.out.println("发起人id==="+historicProcessInstance.getStartUserId());
//            System.out.println("开始节点==="+historicProcessInstance.getStartActivityId());
//            System.out.println("结束节点==="+historicProcessInstance.getEndActivityId());
//            System.out.println("超级流程实例id==="+historicProcessInstance.getSuperProcessInstanceId());
//            System.out.println("删除理由==="+historicProcessInstance.getDeleteReason());

        }

    }


} 
