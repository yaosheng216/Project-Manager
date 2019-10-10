package com.eying.pcss.workflow.dto;

import com.eying.pcss.workflow.constant.FormInputType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 表单输入项
 */
@Data
@ApiModel("FlowFormInput")
public class FlowFormInput implements Serializable {
    @ApiModelProperty("输入类型")
    private FormInputType inputType;
    @ApiModelProperty("是否系统内置")
    private Boolean isBuildIn;
    @ApiModelProperty("业务编码")
    private String code;
    @ApiModelProperty("显示标题")
    private String title;
    @ApiModelProperty("提示信息")
    private String tip;
    @ApiModelProperty("是否必填")
    private Boolean required;
    @ApiModelProperty("可输入最大长度")
    private int maxLength;
    @ApiModelProperty("选项列表")
    private List<String> options;
    @ApiModelProperty("单位")
    private String unit;
    @ApiModelProperty("日期类型（是否需要小时分钟）")
    private Boolean isDateTime;
    @ApiModelProperty("选择部门（员工）是否单选")
    private Boolean isSingleSelect;
    @ApiModelProperty("是否需要自动计算")
    private Boolean isNeedToCalculate;
    @ApiModelProperty("计算公式")
    private String calculateFormula;
}
