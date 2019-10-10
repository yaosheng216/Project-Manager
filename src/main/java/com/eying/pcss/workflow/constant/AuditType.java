package com.eying.pcss.workflow.constant;

import io.swagger.annotations.ApiModel;

/**
 * 审批类型
 */
@ApiModel("AuditType")
public enum AuditType {

    And("会签"),
    Or("或签");

    private String desc;

    AuditType(String _desc){
        this.desc=_desc;
    }

    public String getDesc() {
        return desc;
    }
}
