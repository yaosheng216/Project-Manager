package com.eying.pcss.workflow.dto;

import com.eying.pcss.workflow.constant.AuditPersonType;
import com.eying.pcss.workflow.constant.AuditType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 审批节点定义
 */
@Data
@ApiModel("FlowNodeDef")
public class FlowNodeDef implements Serializable {
    @NotNull
    @ApiModelProperty("审批类型")
    private AuditType auditType;
    @NotBlank
    @ApiModelProperty("节点名称")
    @JsonProperty(value = "name")
    private String name;
    @NotNull
    @ApiModelProperty("审批人类型")
    private AuditPersonType auditPersonType;
    @NotNull
    @ApiModelProperty("审批人列表")
    private List<FlowPerson> auditPersons;
    @ApiModelProperty("抄送人列表")
    private List<FlowPerson> copyPersons;
}
