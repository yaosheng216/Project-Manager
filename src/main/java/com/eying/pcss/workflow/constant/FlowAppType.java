package com.eying.pcss.workflow.constant;

import io.swagger.annotations.ApiModel;

/**
 * 固定应用类型
 */
@ApiModel("FlowAppType")
public enum FlowAppType  {

    Leave("请假"),
    OutWork("外出"),
    OverTime("加班"),
    Card("补卡");

    private String desc;
    FlowAppType(String _desc){
        this.desc=_desc;
    }

    public String getDesc() {
        return desc;
    }
}
