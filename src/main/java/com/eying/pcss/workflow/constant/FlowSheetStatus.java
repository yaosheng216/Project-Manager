package com.eying.pcss.workflow.constant;

import io.swagger.annotations.ApiModel;

/**
 * 审批单状态枚举
 */
@ApiModel("FlowSheetStatus")
public enum FlowSheetStatus {

    Approved("审批通过"),
    Cancel("撤销申请"),
    Reject("审批拒绝"),
    Approving("审批中");

    private String desc;

    FlowSheetStatus(String _desc){
        this.desc=_desc;
    }

    public String getDesc() {
        return desc;
    }
}
