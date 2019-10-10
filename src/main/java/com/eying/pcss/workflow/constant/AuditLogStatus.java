package com.eying.pcss.workflow.constant;

import io.swagger.annotations.ApiModel;

/**
 * 审批单日志状态枚举
 */
@ApiModel("AuditLogStatus")
public enum AuditLogStatus {

    Apply("发起申请"),
    Approved("已同意"),
    Rejected("已拒绝"),
    ReApply("重新申请"),
    Canceled("已撤销");

    private String desc;

    AuditLogStatus(String _desc){
        this.desc=_desc;
    }

    public String getDesc() {
        return desc;
    }
}
