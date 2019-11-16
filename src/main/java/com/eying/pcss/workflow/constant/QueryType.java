package com.eying.pcss.workflow.constant;

import io.swagger.annotations.ApiModel;

/**
 * 查询类型
 */
@ApiModel("QueryType")
public enum QueryType {

    All ("全部"),
    Pending ("待审批"),
    Already ("已审批");

    private String desc;

    QueryType(String _desc) {
        this.desc = _desc;
    }

    public String getDesc() {
        return desc;
    }
}
