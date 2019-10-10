package com.eying.pcss.workflow.service;

import com.eying.pcss.core.util.CommonUtil;
import com.eying.pcss.workflow.bo.FlowDefinitionBO;
import com.eying.pcss.workflow.bo.WorkFlowAppBO;
import com.eying.pcss.workflow.constant.*;
import com.eying.pcss.workflow.dao.FlowDefinitionDao;
import com.eying.pcss.workflow.dto.FlowDepartment;
import com.eying.pcss.workflow.dto.FlowFormInput;
import com.eying.pcss.workflow.dto.FlowNodeDef;
import com.eying.pcss.workflow.dto.FlowPerson;
import com.eying.pcss.workflow.entity.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * WorkFlowAppService Tester.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class WorkFlowAppServiceTest {
    @Autowired
    private WorkFlowAppService appService;
    @Autowired
    private FlowDefinitionDao flowDefinitionDao;
    @Autowired
    private FlowGroupService flowGroupService;
    @Autowired
    private FlowSheetService flowSheetService;

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    @Test
    public void find() {
        String submitId = "5ca45409e21b840001cc0326";
        String auditorId = "";
        String copyUserId = "";
        String companyId = "5cb1588fcff47e000144f81a";
        QueryType queryType = QueryType.All;
        FlowSheetStatus status = null;
        String flowAppId = null;
        String startTime = null;
        String endTime = null;
        Boolean isFinished = null;
        Pageable pageable = CommonUtil.buildPageable(0, 10, "");
        Page<FlowSheet> flowSheetPage= flowSheetService.getApplicantSheets(submitId, auditorId, queryType, copyUserId,companyId, status, flowAppId, startTime, endTime, isFinished, pageable,"");
        System.out.println("flowSheetPage:"+flowSheetPage.toString());
        System.out.println("flowSheetPage:"+flowSheetPage.getContent().toString());

    }

    /**
     * Method: save(WorkFlowApp workFlowApp)
     */
    @Test
    public void testSave() {
        WorkFlowApp app = new WorkFlowApp();
        app.setCompanyId("5cb3feb9cff47e000144f858");
        app.setName("Test");
        List<FlowGroup> groups = flowGroupService.findAll("123456");
        app.setGroup(groups.get(0));
        FlowDepartment dept = new FlowDepartment();
        dept.setDeptId("123");
        dept.setDeptName("123");
        FlowDepartment dept1 = new FlowDepartment();
        dept1.setDeptId("1234");
        dept1.setDeptName("1234");
        app.setVisibleDepartments(new ArrayList<FlowDepartment>() {{
            add(dept);
            add(dept1);
        }});
        FlowPerson person2 = new FlowPerson();
        person2.setStaffId("123");
        person2.setStaffName("123");
        FlowPerson person3 = new FlowPerson();
        person3.setStaffId("1234");
        person3.setStaffName("1234");
        app.setVisibleStaffs(new ArrayList<FlowPerson>() {{
            add(person2);
            add(person3);
        }});
        List<FlowFormInput> inputs = new ArrayList<>();
        FlowFormInput input = new FlowFormInput();
        input.setInputType(FormInputType.Date);
        input.setTitle("Input1");
        input.setRequired(true);
        input.setOptions(new ArrayList<String>() {{
            add("123321");
            add("1234321");
        }});
        FlowFormInput input2 = new FlowFormInput();
        input2.setInputType(FormInputType.SingleText);
        input2.setTitle("Input2");
        input2.setRequired(false);
        input2.setOptions(new ArrayList<String>() {{
            add("123");
            add("1234");
        }});
        inputs.add(input);
        inputs.add(input2);
        app.setInputs(inputs);
        app.setIsCustom(false);
        app.setAppType(FlowAppType.Card);
        List<FlowDefinition> flowDefinitions = flowDefinitionDao.findAll();
        app.setFlowDefinition(flowDefinitions.get(0));
    }

    @Test
    public void publish() {
        WorkFlowAppBO workFlowAppBO = new WorkFlowAppBO();
        //workFlowAppBO.setId("2c9c64fd6ad88025016ad88092d80007");
        workFlowAppBO.setCompanyId("5cb3feb9cff47e000144f858");
        workFlowAppBO.setName("测试");
        workFlowAppBO.setGroupId("2c9c64fd6ad88025016ad88090750000");
        List<FlowNodeDef> nodeDefs = new ArrayList<>();
        FlowNodeDef flowNodeDef = new FlowNodeDef();
        flowNodeDef.setAuditType(AuditType.And);
        flowNodeDef.setName("组长审批");
        flowNodeDef.setAuditPersonType(AuditPersonType.AssignPerson);
        FlowPerson person = new FlowPerson();
        person.setStaffId("5cadbac8cff47e0001c904c8");
        person.setStaffName("5cadbac8cff47e0001c904c8");
        FlowPerson person1 = new FlowPerson();
        person1.setStaffId("5ca59d4d52faff0001a9312f");
        person1.setStaffName("5ca59d4d52faff0001a9312f");
        flowNodeDef.setAuditPersons(new ArrayList<FlowPerson>() {{
            add(person);
            add(person1);
        }});
        FlowPerson person2 = new FlowPerson();
        person2.setStaffId("5ca59d4d52faff0001a9312f");
        person2.setStaffName("5ca59d4d52faff0001a9312f");
        flowNodeDef.setCopyPersons(new ArrayList<FlowPerson>() {{
            add(person2);
        }});

        FlowNodeDef flowNodeDef1 = new FlowNodeDef();
        flowNodeDef1.setAuditType(AuditType.And);
        flowNodeDef1.setName("人事审批");
        flowNodeDef1.setAuditPersonType(AuditPersonType.AssignPerson);
        FlowPerson person3 = new FlowPerson();
        person3.setStaffId("5ca5a04d52faff0001a93130");
        person3.setStaffName("5ca5a04d52faff0001a93130");
        FlowPerson person4 = new FlowPerson();
        person4.setStaffId("5ca5a0ca52faff0001a93131");
        person4.setStaffName("5ca5a0ca52faff0001a93131");
        flowNodeDef1.setAuditPersons(new ArrayList<FlowPerson>() {{
            add(person3);
            add(person4);
        }});

        FlowNodeDef flowNodeDef2 = new FlowNodeDef();
        flowNodeDef2.setAuditType(AuditType.Or);
        flowNodeDef2.setName("经理审批");
        flowNodeDef2.setAuditPersonType(AuditPersonType.AssignPerson);
        FlowPerson person5 = new FlowPerson();
        person5.setStaffId("5ca5a15a52faff0001a93132");
        person5.setStaffName("5ca5a15a52faff0001a93132");
        FlowPerson person6 = new FlowPerson();
        person6.setStaffId("5ca5a32e52faff0001a93133");
        person6.setStaffName("5ca5a32e52faff0001a93133");
        flowNodeDef2.setAuditPersons(new ArrayList<FlowPerson>() {{
            add(person5);
            add(person6);
        }});

        nodeDefs.add(flowNodeDef);
        nodeDefs.add(flowNodeDef1);
        nodeDefs.add(flowNodeDef2);

        FlowDefinitionBO flowDefinitionBO = new FlowDefinitionBO();
        //flowDefinitionBO.setId("2c9c64fd6ad88025016ad88091560003");
        flowDefinitionBO.setIsFree(false);
        flowDefinitionBO.setNodeDefs(nodeDefs);

        workFlowAppBO.setFlow(flowDefinitionBO);
        appService.publish("2c9c64fd6ad88025016ad88092d80007");

    }
}
