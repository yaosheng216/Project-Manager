package com.eying.pcss.workflow.constant;

import io.swagger.annotations.ApiModel;

/**
 * 审批人类型
 */
@ApiModel("AuditPersonType")
public enum AuditPersonType {

    AssignPerson ("指定成员"),
    AssignRole ("指定角色");

    private String desc;

    AuditPersonType(String _desc) {
        this.desc = _desc;
    }

    public String getDesc() {
        return desc;
    }
}
