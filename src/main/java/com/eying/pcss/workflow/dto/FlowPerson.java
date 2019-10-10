package com.eying.pcss.workflow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 流程处理人对象
 */
@Data
@ApiModel("FlowPerson")
public class FlowPerson implements Serializable {
    @ApiModelProperty("处理人员工ID")
    private String staffId;
    @ApiModelProperty("处理人员工姓名")
    private String staffName;
}
