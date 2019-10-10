package com.eying.pcss.workflow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.io.Serializable;
import java.util.List;

/**
 * 审批流程任务节点
 */
@Data
@ApiModel("FlowNode")
public class FlowNode implements Serializable {
    @ApiModelProperty("是否审批通过")
    private Boolean isApproved;
    @ApiModelProperty("任务节点名称")
    private String name;
    @ApiModelProperty("待处理人员工ID列表")
    private List<String> todoPersons;
}
