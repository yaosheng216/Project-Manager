package com.eying.pcss.workflow.bo;

import com.eying.pcss.workflow.dto.FlowNodeDef;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 审批流程定义BO类
 */
@Data
@ApiModel("FlowDefinitionBO")
public class FlowDefinitionBO{
    @ApiModelProperty("是否自由流程")
    private Boolean isFree;
    @NotNull
    @ApiModelProperty("审批节点列表")
    private List<FlowNodeDef> nodeDefs;
}
