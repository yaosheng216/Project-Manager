package com.eying.pcss.workflow.entity;

import com.eying.pcss.workflow.config.JsonbType;
import com.eying.pcss.workflow.constant.FlowSheetStatus;
import com.eying.pcss.workflow.dto.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 自定义审批单
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("FlowSheet")
@Entity
@EntityListeners(AuditingEntityListener.class)
@TypeDefs({
        @TypeDef(name = "JsonbTypeAuditLog", typeClass = JsonbType.class, parameters = {
                @Parameter(name = JsonbType.CLASS, value = "com.eying.pcss.workflow.dto.AuditLog")}),
        @TypeDef(name = "JsonbTypeSheetData", typeClass = JsonbType.class, parameters = {
                @Parameter(name = JsonbType.CLASS, value = "com.eying.pcss.workflow.dto.SheetData")}),
        @TypeDef(name = "JsonbTypeAuditor", typeClass = JsonbType.class, parameters = {
                @Parameter(name = JsonbType.CLASS, value = "com.eying.pcss.workflow.dto.FlowPerson")})
})
public class FlowSheet extends IdEntity {
    @ApiModelProperty("所属企业")
    private String companyId;
    @ApiModelProperty("审批编号")
    private String sheetNumber;
    @ApiModelProperty("申请人ID")
    private String staffId;
    @ApiModelProperty("申请人姓名")
    private String staffName;
    @ApiModelProperty("申请人部门ID")
    private String departmentId;
    @ApiModelProperty("申请人部门名称")
    private String departmentName;
    @ApiModelProperty("申请时间")
    private LocalDateTime submitTime;
    @ApiModelProperty("申请数据")
    @Column(columnDefinition = "json")
    @Type(type = "json")
    private List<SheetData> sheetData;
    @ApiModelProperty("审批日志")
    @Column(columnDefinition = "json")
    @Type(type = "json")
    private List<AuditLog> auditLogs;
    @ApiModelProperty("流程引擎流程实例ID(act_ru_task表中的proc_inst_id关联)")
    private String instanceId;
    @ApiModelProperty("流程应用ID")
    private String flowAppId;
    @Transient
    @ApiModelProperty("流程实例所有任务节点")
    private List<FlowNode> nodes;
    @Transient
    @ApiModelProperty("流程实例当前节点")
    private FlowCurrentNode currentNode;
    @ApiModelProperty("流程是否完成")
    private Boolean isFinished;
    @ApiModelProperty("审批单状态")
    private FlowSheetStatus status;
    @ApiModelProperty("抄送人列表")
    @Column(columnDefinition = "json")
    @Type(type = "json")
    private List<FlowPerson> copyPersons;
    @ApiModelProperty("自由流程审批人列表")
    @Column(columnDefinition = "json")
    @Type(type = "json")
    private List<FlowPerson> auditPersons;
}
