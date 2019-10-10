package com.eying.pcss.workflow.dto;

import com.eying.pcss.workflow.constant.AuditLogStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 审批日志
 */
@Data
@ApiModel("AuditLog")
public class AuditLog implements Serializable {
    @ApiModelProperty("日志状态")
    private AuditLogStatus status;
    @ApiModelProperty("审批时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;
    @ApiModelProperty("审批人id")
    private String auditorId;
    @ApiModelProperty("审批人姓名")
    private String auditorName;
    @ApiModelProperty("审批意见")
    private String comment;
}
