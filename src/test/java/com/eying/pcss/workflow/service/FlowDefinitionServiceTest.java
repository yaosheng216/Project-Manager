package com.eying.pcss.workflow.service;

import com.eying.pcss.workflow.bo.FlowDefinitionBO;
import com.eying.pcss.workflow.dao.FlowDefinitionDao;
import com.eying.pcss.workflow.entity.FlowDefinition;
import com.eying.pcss.workflow.dto.FlowNodeDef;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowDefinitionServiceTest {

    @Autowired
    private WorkFlowAppService appService;

    @Autowired
    private FlowDefinitionDao flowDefinitionDao;

    @Test
    public void save() {
        System.out.println("aaaaaaaaaaaaaaaaaaaaa");
        FlowDefinitionBO flowDefinition = new FlowDefinitionBO();
        FlowNodeDef flowNodeDef = new FlowNodeDef();
        flowNodeDef.setName("节点名称");
        FlowNodeDef flowNodeDef1 = new FlowNodeDef();
        flowNodeDef1.setName("节点名称1");
        FlowNodeDef flowNodeDef3 = new FlowNodeDef();
        flowNodeDef3.setName("节点名称3");
        List<FlowNodeDef> nodeDefs = new ArrayList<>();
        nodeDefs.add(flowNodeDef);
        nodeDefs.add(flowNodeDef1);
        nodeDefs.add(flowNodeDef3);
        System.out.println("flowDefinition==>"+nodeDefs.toString());
        flowDefinition.setNodeDefs(nodeDefs);
        appService.editAppFlow("",flowDefinition);
        System.out.println("bbbbbbbbbbbbbbbbbbbbbbb");
    }

    @Test
    public void getAll(){
        List<FlowDefinition> flowDefinitions = flowDefinitionDao.findAll();
        System.out.println("flowDefinitions=="+flowDefinitions.toString());
    }
}
