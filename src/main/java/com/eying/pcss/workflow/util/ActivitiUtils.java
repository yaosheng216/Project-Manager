package com.eying.pcss.workflow.util;

import org.activiti.bpmn.model.*;
import org.activiti.engine.delegate.BaseTaskListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 生成activiti流程引擎流程定义工具类
 */
public class ActivitiUtils {
    private static final String START_EVENT = "start";
    private static final String END_EVENT = "end";

    /**
     * 创建节点任务 个人任务
     *
     * @param id       任务id标识
     * @param name     任务名称
     * @param assignee 指定个人任务
     * @return
     */
    public static UserTask createUserTask(String id, String name, String assignee) {
        UserTask userTask = new UserTask ();
        userTask.setName (name);
        userTask.setId (id);
        userTask.setAssignee (assignee);
        return userTask;
    }

    /**
     * 创建节点任务 多人任务会签
     *
     * @param id                  任务id标识
     * @param name                任务名称
     * @param assigneeVar         设置任务处理人变量
     * @param inputDataItem       设置任务处理人集合变量
     * @param completionCondition 设置条件<br/>
     *                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${nrOfCompletedInstances/nrOfInstances == 1} 全部都办理才能通过(会签)<br/>
     *                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${nrOfCompletedInstances/nrOfInstances >= 0.6} 按一定比例通过<br/>
     *                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${nrOfCompletedInstances == 1} 有一个处理就通过(或签)<br/>
     * @return
     */
    public static UserTask createUserTask4MultiInstance(String id, String name, String assigneeVar, String inputDataItem, String completionCondition) {
        UserTask userTask = new UserTask ();
        userTask.setName (name);
        userTask.setId (id);
        userTask.setAssignee ("${" + assigneeVar + "}");
        MultiInstanceLoopCharacteristics loopCharacteristics = new MultiInstanceLoopCharacteristics ();
        loopCharacteristics.setSequential (false);
        loopCharacteristics.setElementVariable (assigneeVar);
        loopCharacteristics.setInputDataItem ("${" + inputDataItem + "}");
        loopCharacteristics.setCompletionCondition (completionCondition);
        userTask.setLoopCharacteristics (loopCharacteristics);
        return userTask;
    }

    /**
     * 创建节点任务 多人任务会签
     *
     * @param id                  任务id标识
     * @param name                任务名称
     * @param assigneeVar         设置任务处理人变量
     * @param inputDataItem       设置任务处理人集合变量
     * @param completionCondition 设置条件
     * @param taskListenerList    监听的集合,TaskListener实现类的的具体路径例<br/>
     *                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${nrOfCompletedInstances/nrOfInstances == 1} 全部都办理才能通过(会签)<br/>
     *                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${nrOfCompletedInstances/nrOfInstances >= 0.6} 按一定比例通过<br/>
     *                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${nrOfCompletedInstances == 1} 有一个处理就通过(或签)<br/>
     * @return
     */
    public static UserTask createUserTask4MultiInstance(String id, String name, String assigneeVar,
                                                        String inputDataItem, String completionCondition, List<String> taskListenerList) {
        UserTask userTask = new UserTask ();
        userTask.setName (name);
        userTask.setId (id);
        userTask.setAssignee ("${" + assigneeVar + "}");
        MultiInstanceLoopCharacteristics loopCharacteristics = new MultiInstanceLoopCharacteristics ();
        loopCharacteristics.setSequential (false);
        loopCharacteristics.setElementVariable (assigneeVar);
        loopCharacteristics.setInputDataItem ("${" + inputDataItem + "}");
        loopCharacteristics.setCompletionCondition (completionCondition);
        userTask.setLoopCharacteristics (loopCharacteristics);
        List<ActivitiListener> list = new ArrayList<> ();
        for (String taskListener : taskListenerList) {
            ActivitiListener listener = new ActivitiListener ();
            listener.setEvent (BaseTaskListener.EVENTNAME_COMPLETE);
            //Spring配置以变量形式调用无法写入，只能通过继承TaskListener方法，
            listener.setImplementationType ("class");
            listener.setImplementation (taskListener);
            list.add (listener);
        }
        userTask.setTaskListeners (list);
        return userTask;
    }

    /**
     * 创建节点任务 多人任务
     *
     * @param id             任务id标识
     * @param name           任务名称
     * @param candidateUsers 任务人的集合
     * @return
     */
    public static UserTask createUserTask4Users(String id, String name, String[] candidateUsers) {
        UserTask userTask = new UserTask ();
        userTask.setName (name);
        userTask.setId (id);
        if (candidateUsers != null && candidateUsers.length > 0) {
            userTask.setCandidateUsers (Arrays.asList (candidateUsers));
        }
        return userTask;
    }

    /**
     * 创建节点任务 组或角色任务
     *
     * @param id              任务id标识
     * @param name            任务名称
     * @param candidateGroups 任务人的组或角色集合
     * @return
     */
    public static UserTask createUserTask4Groups(String id, String name, String[] candidateGroups) {
        UserTask userTask = new UserTask ();
        userTask.setName (name);
        userTask.setId (id);
        if (candidateGroups != null && candidateGroups.length > 0) {
            userTask.setCandidateGroups (Arrays.asList (candidateGroups));
        }
        return userTask;
    }

    /**
     * 设置连线
     *
     * @param from 从哪里出发
     * @param to   连接到哪里
     * @return
     */
    public static SequenceFlow createSequenceFlow(String from, String to) {
        SequenceFlow flow = new SequenceFlow ();
        flow.setSourceRef (from);
        flow.setTargetRef (to);
        return flow;
    }

    /**
     * 设置起始节点
     *
     * @return
     */
    public static StartEvent createStartEvent() {
        StartEvent startEvent = new StartEvent ();
        startEvent.setId (START_EVENT);
        return startEvent;
    }

    /**
     * 排他网关节点
     *
     * @param id
     * @return
     */
    public static ExclusiveGateway createExclusiveGateway(String id) {
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway ();
        exclusiveGateway.setId (id);
        return exclusiveGateway;
    }

    /**
     * 设置结束节点
     *
     * @return
     */
    public static EndEvent createEndEvent() {
        EndEvent endEvent = new EndEvent ();
        endEvent.setId (END_EVENT);
        return endEvent;
    }

    /**
     * 设置连线
     *
     * @param from                从哪里出发
     * @param to                  连接到哪里
     * @param name                连线名称
     * @param conditionExpression 判断条件${arg>2}
     * @return
     */
    public static SequenceFlow createSequenceFlow(String from, String to, String name, String conditionExpression) {
        SequenceFlow flow = new SequenceFlow ();
        flow.setSourceRef (from);
        flow.setTargetRef (to);
        flow.setName (name);
        if (conditionExpression != null && !"".equals (conditionExpression)) {
            flow.setConditionExpression (conditionExpression);
        }
        return flow;
    }
}
