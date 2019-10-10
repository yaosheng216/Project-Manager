package com.eying.pcss.workflow.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * 审批流程分组初始化数据表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("FlowGroupInit")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class FlowGroupInit extends IdEntity{
    @ApiModelProperty("分组名称")
    private String name;
    @ApiModelProperty("是否内置")
    private Boolean isBuildIn;
}
