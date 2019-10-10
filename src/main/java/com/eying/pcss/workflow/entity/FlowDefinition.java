package com.eying.pcss.workflow.entity;

import com.eying.pcss.workflow.dto.FlowNodeDef;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.util.List;

/**
 * 审批流程定义
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("FlowDefinition")
@Entity
@EntityListeners(AuditingEntityListener.class)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class FlowDefinition extends IdEntity{
    @ApiModelProperty("企业ID")
    private String companyId;
    @ApiModelProperty("是否自由流程")
    private Boolean isFree;
    @ApiModelProperty("审批节点列表")
    @Column(columnDefinition = "json")
    @Type(type = "json")
    private List<FlowNodeDef> nodeDefs;
    @ApiModelProperty("流程引擎流程定义key_(act_re_procdef表中的key关联)")
    private String actDefId;
}
