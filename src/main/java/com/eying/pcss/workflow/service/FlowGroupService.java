package com.eying.pcss.workflow.service;

import com.eying.pcss.core.exception.GenExceptCode;
import com.eying.pcss.core.exception.ServiceException;
import com.eying.pcss.core.util.CommonUtil;
import com.eying.pcss.workflow.dao.FlowGroupDao;
import com.eying.pcss.workflow.dao.WorkFlowAppDao;
import com.eying.pcss.workflow.entity.FlowGroup;
import com.eying.pcss.workflow.entity.QFlowGroup;
import com.eying.pcss.workflow.entity.QWorkFlowApp;
import com.eying.pcss.workflow.entity.WorkFlowApp;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审批流程分组Service类
 */
@Service
public class FlowGroupService {

    private final FlowGroupDao flowGroupDao;
    private final WorkFlowAppDao appDao;

    @Autowired
    public FlowGroupService(FlowGroupDao flowGroupDao, WorkFlowAppDao appDao) {
        this.flowGroupDao = flowGroupDao;
        this.appDao = appDao;
    }

    /**
     * 创建流程分组
     *
     * @param name
     * @param companyId
     */
    public String saveFlowGroup(String name, String companyId) {
        FlowGroup flowGroup = new FlowGroup ();
        flowGroup.setCompanyId (companyId);
        flowGroup.setName (name);
        flowGroup.setIsBuildIn (false);
        Predicate predicate = QFlowGroup.flowGroup.name.eq (name)
                .and (QFlowGroup.flowGroup.companyId.eq (flowGroup.getCompanyId ()));
        if (flowGroupDao.findOne (predicate).isPresent ()) {
            throw new ServiceException (GenExceptCode.Request_Param);
        }
        return flowGroupDao.save (flowGroup).getId ().toString ();
    }

    /**
     * 查询应用分组详情
     *
     * @param id
     * @return
     */
    public FlowGroup getFlowGroup(Long id) {
        Assert.hasText (id.toString (), GenExceptCode.Request_Param.getMsg ());
        return flowGroupDao.findById (id).orElse (null);
    }

    /**
     * 查询流程分组
     *
     * @return
     */
    public List<FlowGroup> findAll(String companyId) {
        Predicate predicate = null;
        if (StringUtils.isNotBlank (companyId)) {
            predicate = QFlowGroup.flowGroup.companyId.eq (companyId);
        }
        Pageable pageable = CommonUtil.buildPageable (0, Integer.MAX_VALUE, "isBuildIn|asc,createTime|asc");
        if (predicate == null) {
            return flowGroupDao.findAll (pageable).getContent ();
        }
        return Lists.newArrayList (flowGroupDao.findAll (predicate, pageable).getContent ());
    }

    /**
     * 修改流程分组名称
     *
     * @param id
     * @param name
     */
    public void modifyGroupName(Long id, String name) {
        Assert.hasText (id.toString (), GenExceptCode.Request_Param.getMsg ());
        FlowGroup flowGroup = flowGroupDao.findById (Long.valueOf (id)).orElse (null);
        if (flowGroup == null) {
            throw new ServiceException (GenExceptCode.Request_Param.name (), FlowGroup.class.getSimpleName ());
        }
        Predicate predicate = QFlowGroup.flowGroup.name.eq (name).and (QFlowGroup.flowGroup.id.ne (id))
                .and (QFlowGroup.flowGroup.companyId.eq (flowGroup.getCompanyId ()));
        if (flowGroupDao.findOne (predicate).isPresent ()) {
            throw new ServiceException (GenExceptCode.Request_Param);
        }
        flowGroup.setName (name);
        flowGroupDao.save (flowGroup);
    }

    /**
     * 删除流程分组，并将其下面的流程应用移动到其他分组中
     *
     * @param id
     */
    public void deleteFlowGroupById(Long id) {
        Assert.hasText (id.toString (), GenExceptCode.Request_Param.getMsg ());
        FlowGroup flowGroup = flowGroupDao.findById (id).orElse (null);
        if (flowGroup == null) {
            throw new ServiceException (GenExceptCode.Request_Param.name (), FlowGroup.class.getSimpleName ());
        }
        // 删除分组下面的所有应用
        Predicate predicate = QWorkFlowApp.workFlowApp.group.id.eq (id);
        List<WorkFlowApp> apps = Lists.newArrayList (appDao.findAll (predicate));
        Predicate otherPredicate = QFlowGroup.flowGroup.isBuildIn.isTrue ()
                .and (QFlowGroup.flowGroup.name.eq ("其他"))
                .and (QFlowGroup.flowGroup.companyId.eq (flowGroup.getCompanyId ()));
        FlowGroup group = flowGroupDao.findOne (otherPredicate).orElse (null);
        for (WorkFlowApp app : apps) {
            app.setGroup (group);
        }
        appDao.saveAll (apps);
        flowGroupDao.delete (flowGroup);
    }
}
