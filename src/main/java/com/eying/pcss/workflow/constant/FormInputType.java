package com.eying.pcss.workflow.constant;

import io.swagger.annotations.ApiModel;

/**
 * 表单控件类型
 */
@ApiModel("FormInputType")
public enum FormInputType {

    SingleText ("单行输入"),
    MultiText ("多行输入"),
    Digital ("数字输入"),
    SingleSelect ("单选"),
    MultiSelect ("多选"),
    Date ("日期"),
    Picture ("图片上传"),
    File ("文件上传"),
    DepartSelect ("选择部门"),
    StaffSelect ("选择员工");

    private String desc;

    FormInputType(String _desc) {
        this.desc = _desc;
    }

    public String getDesc() {
        return desc;
    }
}
