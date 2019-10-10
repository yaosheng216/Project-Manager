package com.eying.pcss.workflow.entity;

import com.eying.pcss.workflow.constant.FlowAppType;
import com.eying.pcss.workflow.dto.FlowFormInput;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

/**
 * 审批流程应用初始化数据表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("WorkFlowAppInit")
@Entity
@EntityListeners(AuditingEntityListener.class)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class WorkFlowAppInit extends IdEntity {
    @ApiModelProperty("应用名称")
    private String name;
    @ApiModelProperty("应用分组")
    @OneToOne(targetEntity = FlowGroupInit.class)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private FlowGroupInit group;
    @ApiModelProperty("说明")
    private String description;
    @ApiModelProperty("应用图标")
    private String icon;
    @ApiModelProperty("是否自定义")
    private Boolean isCustom;
    @ApiModelProperty("固定应用类型")
    private FlowAppType appType;
    @ApiModelProperty("是否全部员工可见")
    private Boolean isAllVisible;
    @ApiModelProperty("表单输入项列表")
    @Column(columnDefinition = "json")
    @Type(type = "json")
    private List<FlowFormInput> inputs;
    @ApiModelProperty("是否启用应用")
    private Boolean isEnabled;
}
