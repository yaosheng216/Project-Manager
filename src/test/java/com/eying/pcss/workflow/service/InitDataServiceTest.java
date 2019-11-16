package com.eying.pcss.workflow.service;

import com.eying.pcss.workflow.constant.FlowAppType;
import com.eying.pcss.workflow.constant.FormInputType;
import com.eying.pcss.workflow.dao.FlowGroupInitDao;
import com.eying.pcss.workflow.dao.WorkFlowAppInitDao;
import com.eying.pcss.workflow.dto.FlowFormInput;
import com.eying.pcss.workflow.entity.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化数据Tester.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class InitDataServiceTest {
    @Autowired
    private FlowGroupInitDao groupInitDao;
    @Autowired
    private WorkFlowAppInitDao appInitDao;

    private FlowGroupInit group;

    @Before
    public void before() {
        groupInitDao.deleteAll ();
        FlowGroupInit group1 = new FlowGroupInit ();
        group1.setName ("其他");
        group1.setIsBuildIn (true);
        groupInitDao.save (group1);
        FlowGroupInit group2 = new FlowGroupInit ();
        group2.setName ("人事审批");
        group2.setIsBuildIn (false);
        group = groupInitDao.save (group2);
    }

    @After
    public void after() {
    }

    /**
     * 创建流程应用初始化数据
     */
    @Test
    public void testCreateWorkFlowApp() {
        appInitDao.deleteAll ();
        // 请假申请应用
        WorkFlowAppInit leaveApp = new WorkFlowAppInit ();
        leaveApp.setName ("请假");
        leaveApp.setGroup (group);
        leaveApp.setAppType (FlowAppType.Leave);
        leaveApp.setDescription ("请假申请");
        leaveApp.setIcon ("icon-qingjia-BACFF-");
        leaveApp.setIsCustom (false);
        leaveApp.setIsAllVisible (true);
        leaveApp.setIsEnabled (true);
        FlowFormInput leaveInput1 = new FlowFormInput ();
        leaveInput1.setInputType (FormInputType.SingleSelect);
        leaveInput1.setIsBuildIn (true);
        leaveInput1.setCode ("LeaveType");
        leaveInput1.setTitle ("请假类型");
        leaveInput1.setTip ("请选择");
        leaveInput1.setRequired (true);
        List<String> options = new ArrayList<> ();
        options.add ("事假");
        options.add ("病假");
        options.add ("调休");
        options.add ("年假");
        options.add ("婚假");
        options.add ("产假");
        options.add ("陪产假");
        leaveInput1.setOptions (options);
        FlowFormInput leaveInput2 = new FlowFormInput ();
        leaveInput2.setInputType (FormInputType.Date);
        leaveInput2.setIsBuildIn (true);
        leaveInput2.setCode ("LeaveStartTime");
        leaveInput2.setTitle ("请假开始时间");
        leaveInput2.setTip ("请选择时间");
        leaveInput2.setRequired (true);
        leaveInput2.setIsDateTime (true);
        FlowFormInput leaveInput3 = new FlowFormInput ();
        leaveInput3.setInputType (FormInputType.Date);
        leaveInput3.setIsBuildIn (true);
        leaveInput3.setCode ("LeaveEndTime");
        leaveInput3.setTitle ("请假结束时间");
        leaveInput3.setTip ("请选择时间");
        leaveInput3.setRequired (true);
        leaveInput3.setIsDateTime (true);
        FlowFormInput leaveInput4 = new FlowFormInput ();
        leaveInput4.setInputType (FormInputType.Digital);
        leaveInput4.setIsBuildIn (true);
        leaveInput4.setCode ("Duration");
        leaveInput4.setTitle ("时长（天）");
        leaveInput4.setTip ("请假时长将自动计入考勤统计");
        leaveInput4.setRequired (true);
        leaveInput4.setUnit ("天");
        FlowFormInput leaveInput5 = new FlowFormInput ();
        leaveInput5.setInputType (FormInputType.MultiText);
        leaveInput5.setIsBuildIn (true);
        leaveInput5.setCode ("LeaveReason");
        leaveInput5.setTitle ("请假事由");
        leaveInput5.setTip ("请输入请假事由");
        leaveInput5.setRequired (true);
        FlowFormInput leaveInput6 = new FlowFormInput ();
        leaveInput6.setInputType (FormInputType.Picture);
        leaveInput6.setIsBuildIn (true);
        leaveInput6.setCode ("LeavePicture");
        leaveInput6.setTitle ("请假相关图片");
        leaveInput6.setTip ("图片最多可添加9张");
        leaveInput6.setRequired (false);
        leaveInput6.setMaxLength (9);
        FlowFormInput leaveInput7 = new FlowFormInput ();
        leaveInput7.setInputType (FormInputType.DepartSelect);
        leaveInput7.setIsBuildIn (true);
        leaveInput7.setCode ("Department");
        leaveInput7.setTitle ("所在部门");
        leaveInput7.setTip ("请选择部门");
        leaveInput7.setRequired (true);
        leaveInput7.setIsSingleSelect (true);
        List<FlowFormInput> leaveInputs = new ArrayList<> ();
        leaveInputs.add (leaveInput1);
        leaveInputs.add (leaveInput2);
        leaveInputs.add (leaveInput3);
        leaveInputs.add (leaveInput4);
        leaveInputs.add (leaveInput5);
        leaveInputs.add (leaveInput6);
        leaveInputs.add (leaveInput7);
        leaveApp.setInputs (leaveInputs);

        // 补卡申请
        WorkFlowAppInit cardApp = new WorkFlowAppInit ();
        cardApp.setName ("补卡");
        cardApp.setGroup (group);
        cardApp.setAppType (FlowAppType.Card);
        cardApp.setDescription ("补卡申请");
        cardApp.setIcon ("icon-qingjia-BACFF-");
        cardApp.setIsCustom (false);
        cardApp.setIsAllVisible (true);
        cardApp.setIsEnabled (true);
        FlowFormInput cardInput1 = new FlowFormInput ();
        cardInput1.setInputType (FormInputType.Date);
        cardInput1.setIsBuildIn (true);
        cardInput1.setCode ("CardTime");
        cardInput1.setTitle ("补卡时间点");
        cardInput1.setTip ("请选择时间");
        cardInput1.setRequired (true);
        cardInput1.setIsDateTime (true);
        FlowFormInput cardInput2 = new FlowFormInput ();
        cardInput2.setInputType (FormInputType.MultiText);
        cardInput2.setIsBuildIn (true);
        cardInput2.setCode ("CardReason");
        cardInput2.setTitle ("补卡原因");
        cardInput2.setTip ("请输入补卡原因");
        cardInput2.setRequired (true);
        FlowFormInput cardInput3 = new FlowFormInput ();
        cardInput3.setInputType (FormInputType.Picture);
        cardInput3.setIsBuildIn (true);
        cardInput3.setCode ("CardPicture");
        cardInput3.setTitle ("补卡相关图片");
        cardInput3.setTip ("图片最多可添加9张");
        cardInput3.setRequired (false);
        cardInput3.setMaxLength (9);
        FlowFormInput cardInput4 = new FlowFormInput ();
        cardInput4.setInputType (FormInputType.DepartSelect);
        cardInput4.setIsBuildIn (true);
        cardInput4.setCode ("Department");
        cardInput4.setTitle ("所在部门");
        cardInput4.setTip ("请选择部门");
        cardInput4.setRequired (true);
        cardInput4.setIsSingleSelect (true);
        List<FlowFormInput> cardInputs = new ArrayList<> ();
        cardInputs.add (cardInput1);
        cardInputs.add (cardInput2);
        cardInputs.add (cardInput3);
        cardInputs.add (cardInput4);
        cardApp.setInputs (cardInputs);

        // 外出申请
        WorkFlowAppInit outWorkApp = new WorkFlowAppInit ();
        outWorkApp.setName ("外出");
        outWorkApp.setGroup (group);
        outWorkApp.setAppType (FlowAppType.OutWork);
        outWorkApp.setDescription ("外出申请");
        outWorkApp.setIcon ("icon-waichu-F-");
        outWorkApp.setIsCustom (false);
        outWorkApp.setIsAllVisible (true);
        outWorkApp.setIsEnabled (true);
        FlowFormInput outWorkInput1 = new FlowFormInput ();
        outWorkInput1.setInputType (FormInputType.Date);
        outWorkInput1.setIsBuildIn (true);
        outWorkInput1.setCode ("OutWorkStartTime");
        outWorkInput1.setTitle ("外出开始时间");
        outWorkInput1.setTip ("请选择时间");
        outWorkInput1.setRequired (true);
        outWorkInput1.setIsDateTime (true);
        FlowFormInput outWorkInput2 = new FlowFormInput ();
        outWorkInput2.setInputType (FormInputType.Date);
        outWorkInput2.setIsBuildIn (true);
        outWorkInput2.setCode ("OutWorkEndTime");
        outWorkInput2.setTitle ("外出结束时间");
        outWorkInput2.setTip ("请选择时间");
        outWorkInput2.setRequired (true);
        outWorkInput2.setIsDateTime (true);
        FlowFormInput outWorkInput3 = new FlowFormInput ();
        outWorkInput3.setInputType (FormInputType.MultiText);
        outWorkInput3.setIsBuildIn (true);
        outWorkInput3.setCode ("OutWorkReason");
        outWorkInput3.setTitle ("外出事由");
        outWorkInput3.setTip ("请输入外出事由");
        outWorkInput3.setRequired (true);
        FlowFormInput outWorkInput4 = new FlowFormInput ();
        outWorkInput4.setInputType (FormInputType.Picture);
        outWorkInput4.setIsBuildIn (true);
        outWorkInput4.setCode ("OutWorkPicture");
        outWorkInput4.setTitle ("外出相关图片");
        outWorkInput4.setTip ("图片最多可添加9张");
        outWorkInput4.setRequired (false);
        outWorkInput4.setMaxLength (9);
        FlowFormInput outWorkInput5 = new FlowFormInput ();
        outWorkInput5.setInputType (FormInputType.DepartSelect);
        outWorkInput5.setIsBuildIn (true);
        outWorkInput5.setCode ("Department");
        outWorkInput5.setTitle ("所在部门");
        outWorkInput5.setTip ("请选择部门");
        outWorkInput5.setRequired (true);
        outWorkInput5.setIsSingleSelect (true);
        FlowFormInput outWorkInput6 = new FlowFormInput ();
        outWorkInput6.setInputType (FormInputType.Digital);
        outWorkInput6.setIsBuildIn (true);
        outWorkInput6.setCode ("Duration");
        outWorkInput6.setTitle ("时长（天）");
        outWorkInput6.setTip ("根据排班时间自动计算时长");
        outWorkInput6.setRequired (true);
        outWorkInput6.setUnit ("天");
        List<FlowFormInput> outWorkInputs = new ArrayList<> ();
        outWorkInputs.add (outWorkInput1);
        outWorkInputs.add (outWorkInput2);
        outWorkInputs.add (outWorkInput3);
        outWorkInputs.add (outWorkInput4);
        outWorkInputs.add (outWorkInput5);
        outWorkInputs.add (outWorkInput6);
        outWorkApp.setInputs (outWorkInputs);

        // 加班申请
        WorkFlowAppInit overTimeApp = new WorkFlowAppInit ();
        overTimeApp.setName ("加班");
        overTimeApp.setGroup (group);
        overTimeApp.setAppType (FlowAppType.OverTime);
        overTimeApp.setDescription ("加班申请");
        overTimeApp.setIcon ("icon-jiaban-1");
        overTimeApp.setIsCustom (false);
        overTimeApp.setIsAllVisible (true);
        overTimeApp.setIsEnabled (true);
        FlowFormInput overTimeInput1 = new FlowFormInput ();
        overTimeInput1.setInputType (FormInputType.Date);
        overTimeInput1.setIsBuildIn (true);
        overTimeInput1.setCode ("OverTimeStartTime");
        overTimeInput1.setTitle ("加班开始时间");
        overTimeInput1.setTip ("请选择时间");
        overTimeInput1.setRequired (true);
        overTimeInput1.setIsDateTime (true);
        FlowFormInput overTimeInput2 = new FlowFormInput ();
        overTimeInput2.setInputType (FormInputType.Date);
        overTimeInput2.setIsBuildIn (true);
        overTimeInput2.setCode ("OverTimeEndTime");
        overTimeInput2.setTitle ("加班结束时间");
        overTimeInput2.setTip ("请选择时间");
        overTimeInput2.setRequired (true);
        overTimeInput2.setIsDateTime (true);
        FlowFormInput overTimeInput3 = new FlowFormInput ();
        overTimeInput3.setInputType (FormInputType.MultiText);
        overTimeInput3.setIsBuildIn (true);
        overTimeInput3.setCode ("OverTimeReason");
        overTimeInput3.setTitle ("加班事由");
        overTimeInput3.setTip ("请输入加班事由");
        overTimeInput3.setRequired (true);
        FlowFormInput overTimeInput4 = new FlowFormInput ();
        overTimeInput4.setInputType (FormInputType.Picture);
        overTimeInput4.setIsBuildIn (true);
        overTimeInput4.setCode ("OverTimePicture");
        overTimeInput4.setTitle ("加班相关图片");
        overTimeInput4.setTip ("图片最多可添加9张");
        overTimeInput4.setRequired (false);
        overTimeInput4.setMaxLength (9);
        FlowFormInput overTimeInput5 = new FlowFormInput ();
        overTimeInput5.setInputType (FormInputType.DepartSelect);
        overTimeInput5.setIsBuildIn (true);
        overTimeInput5.setCode ("Department");
        overTimeInput5.setTitle ("所在部门");
        overTimeInput5.setTip ("请选择部门");
        overTimeInput5.setRequired (true);
        overTimeInput5.setIsSingleSelect (true);
        FlowFormInput overTimeInput6 = new FlowFormInput ();
        overTimeInput6.setInputType (FormInputType.Digital);
        overTimeInput6.setIsBuildIn (true);
        overTimeInput6.setCode ("Duration");
        overTimeInput6.setTitle ("时长（天）");
        overTimeInput6.setTip ("根据排班时间自动计算时长");
        overTimeInput6.setRequired (true);
        overTimeInput6.setUnit ("天");
        List<FlowFormInput> overTimeInputs = new ArrayList<> ();
        overTimeInputs.add (overTimeInput1);
        overTimeInputs.add (overTimeInput2);
        overTimeInputs.add (overTimeInput3);
        overTimeInputs.add (overTimeInput4);
        overTimeInputs.add (overTimeInput5);
        overTimeInputs.add (overTimeInput6);
        overTimeApp.setInputs (overTimeInputs);

        List<WorkFlowAppInit> workFlowApps = new ArrayList<> ();
        workFlowApps.add (leaveApp);
        workFlowApps.add (cardApp);
        workFlowApps.add (outWorkApp);
        workFlowApps.add (overTimeApp);
        appInitDao.saveAll (workFlowApps);
    }
}
