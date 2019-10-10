package com.eying.pcss.workflow.dto;

import com.eying.pcss.workflow.constant.AuditType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 审批流程当前任务节点
 */
@Data
@ApiModel("FlowCurrentNode")
public class FlowCurrentNode implements Serializable {
    @ApiModelProperty("审批类型")
    private AuditType auditType;
    @ApiModelProperty("任务节点名称")
    private String name;
    @ApiModelProperty("待处理人员工ID列表")
    private List<String> todoPersons;
}
