package com.eying.pcss.workflow.bo;

import com.eying.pcss.workflow.dto.FlowPerson;
import com.eying.pcss.workflow.dto.SheetData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 自定义审批单BO类
 */
@Data
@ApiModel("FlowSheetBO")
public class FlowSheetBO {
    @NotBlank
    @ApiModelProperty("所属企业")
    private String companyId;
    @NotBlank
    @ApiModelProperty("申请人ID")
    private String staffId;
    @NotBlank
    @ApiModelProperty("申请人姓名")
    private String staffName;
    @NotBlank
    @ApiModelProperty("申请人部门ID")
    private String departmentId;
    @NotBlank
    @ApiModelProperty("申请人部门名称")
    private String departmentName;
    @ApiModelProperty("申请数据")
    private List<SheetData> sheetData;
    @NotBlank
    @ApiModelProperty("流程应用ID")
    private String flowAppId;
    @ApiModelProperty("自由流程抄送人列表")
    private List<FlowPerson> copyPersons;
    @ApiModelProperty("自由流程审批人列表")
    private List<FlowPerson> auditPersons;
}
