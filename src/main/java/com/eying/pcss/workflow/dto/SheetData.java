package com.eying.pcss.workflow.dto;

import com.eying.pcss.workflow.constant.FormInputType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 审批表单申请数据
 */
@Data
@ApiModel("SheetData")
public class SheetData implements Serializable {
    @ApiModelProperty("输入类型")
    private FormInputType inputType;
    @ApiModelProperty("业务编码")
    private String code;
    @ApiModelProperty("输入项标题")
    private String title;
    @ApiModelProperty("输入项的值（多个值时使用json数组字符串，如果是附件则传入[{name:'',uri:'',mimeType:''}]格式数组json字符串）")
    private String value;
}
