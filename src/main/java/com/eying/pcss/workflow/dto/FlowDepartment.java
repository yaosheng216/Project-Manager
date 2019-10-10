package com.eying.pcss.workflow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 流程部门对象
 */
@Data
@ApiModel("FlowDepartment")
public class FlowDepartment implements Serializable {
    @ApiModelProperty("部门ID")
    private String deptId;
    @ApiModelProperty("部门名称")
    private String deptName;
}
