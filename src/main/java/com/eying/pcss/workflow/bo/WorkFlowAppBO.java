package com.eying.pcss.workflow.bo;

import com.eying.pcss.workflow.dto.FlowDepartment;
import com.eying.pcss.workflow.dto.FlowFormInput;
import com.eying.pcss.workflow.dto.FlowPerson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 审批流程应用BO类
 */
@Data
@ApiModel("WorkFlowAppBO")
public class WorkFlowAppBO {
    @ApiModelProperty("流程应用ID")
    private String id;
    @NotBlank
    @ApiModelProperty("所属企业")
    private String companyId;
    @NotBlank
    @ApiModelProperty("应用名称")
    private String name;
    @NotBlank
    @ApiModelProperty("应用分组")
    private String groupId;
    @ApiModelProperty("说明")
    private String description;
    @ApiModelProperty("可见部门列表")
    private List<FlowDepartment> visibleDepartments;
    @ApiModelProperty("可见员工列表")
    private List<FlowPerson> visibleStaffs;
    @ApiModelProperty("是否全部员工可见")
    private Boolean isAllVisible;
    @ApiModelProperty("应用图标")
    private String icon;
    @ApiModelProperty("是否自定义")
    private Boolean isCustom;
    @ApiModelProperty("表单输入项列表")
    private List<FlowFormInput> inputs;
    @ApiModelProperty("流程定义")
    private FlowDefinitionBO flow;
}
