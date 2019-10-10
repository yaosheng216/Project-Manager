package com.eying.pcss.workflow.service;

import com.eying.pcss.core.util.CopyUtil;
import com.eying.pcss.workflow.dao.*;
import com.eying.pcss.workflow.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 企业审批初始化管理Service类
 */
@Service
public class FlowInitDataService {

    private final FlowGroupDao groupDao;
    private final WorkFlowAppDao workFlowAppDao;
    private final FlowDefinitionDao flowDefinitionDao;
    private final WorkFlowAppInitDao appInitDao;
    private final FlowGroupInitDao groupInitDao;

    @Autowired
    public FlowInitDataService(FlowGroupDao groupDao, WorkFlowAppDao workFlowAppDao,
                               FlowDefinitionDao flowDefinitionDao, WorkFlowAppInitDao appInitDao,
                               FlowGroupInitDao groupInitDao) {
        this.groupDao = groupDao;
        this.workFlowAppDao = workFlowAppDao;
        this.flowDefinitionDao = flowDefinitionDao;
        this.appInitDao = appInitDao;
        this.groupInitDao = groupInitDao;
    }

    /**
     * 初始化企业流程数据
     *
     * @param companyId 企业id
     */
    public void initData(String companyId) {
        List<WorkFlowAppInit> appInitList = appInitDao.findAll();
        List<FlowGroupInit> groupInitList = groupInitDao.findAll();
        Map<String,FlowGroup> groupMap = new HashMap<>();
        for (FlowGroupInit groupInit : groupInitList){
            FlowGroup group = new FlowGroup();
            CopyUtil.copyNotNullProperties(groupInit, group);
            group.setCompanyId(companyId);
//            group.setId(null);
            groupDao.save(group);
            groupMap.put(groupInit.getName(), group);
        }
        List<WorkFlowApp> apps = new ArrayList<>();
        for (WorkFlowAppInit appInit : appInitList){
            WorkFlowApp workFlowApp = new WorkFlowApp();
            CopyUtil.copyNotNullProperties(appInit, workFlowApp);
            workFlowApp.setCompanyId(companyId);
            FlowDefinition definition = new FlowDefinition();
            definition.setCompanyId(companyId);
            definition.setIsFree(true);
            flowDefinitionDao.save(definition);
            workFlowApp.setFlowDefinition(definition);

            FlowGroupInit groupInit = appInit.getGroup();
            workFlowApp.setGroup(groupMap.get(groupInit.getName()));
            apps.add(workFlowApp);
        }
        workFlowAppDao.saveAll(apps);
    }

}
