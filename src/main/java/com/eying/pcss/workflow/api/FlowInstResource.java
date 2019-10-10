package com.eying.pcss.workflow.api;

import com.eying.pcss.core.api.AbstractResource;
import com.eying.pcss.core.util.CommonUtil;
import com.eying.pcss.workflow.entity.FlowSheet;
import com.eying.pcss.workflow.constant.FlowSheetStatus;
import com.eying.pcss.workflow.constant.QueryType;
import com.eying.pcss.workflow.bo.FlowSheetBO;
import com.eying.pcss.workflow.service.FlowSheetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 审批实例接口
 */
@Component
@Path("/instances")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "审批流程实例管理", produces = "application/json")
public class FlowInstResource extends AbstractResource {

    private final FlowSheetService flowSheetService;

    @Autowired
    public FlowInstResource(FlowSheetService flowSheetService) {
        this.flowSheetService = flowSheetService;
    }

    @POST
    @Path("/createInstance")
    @ApiOperation("发起审批")
    public Response createInstance(
            FlowSheetBO sheet
    ) {
        return this.successCreate(flowSheetService.createInstance(sheet));
    }

    @POST
    @Path("/getAppSheets")
    @ApiOperation("查询审批表单列表")
    public Page<FlowSheet> getApplicantSheets(
            @QueryParam("submitId") String submitId,
            @QueryParam("auditorId") String auditorId,
            @QueryParam("queryType") QueryType queryType,
            @QueryParam("copyUserId") String copyUserId,
            @QueryParam("companyId") String companyId,
            @QueryParam("status") FlowSheetStatus status,
            @QueryParam("flowAppId") String flowAppId,
            @QueryParam("startTime") String startTime,
            @QueryParam("endTime") String endTime,
            @QueryParam("isFinished") Boolean isFinished,
            @QueryParam("page") int page,
            @QueryParam("size") int size,
            @QueryParam("order") String order
    ) {
        Pageable pageable = CommonUtil.buildPageable(page, size, order);
        return flowSheetService.getApplicantSheets(submitId, auditorId, queryType, copyUserId,
                companyId, status, flowAppId, startTime, endTime, isFinished, pageable,order);
    }

    @GET
    @Path("/getFlowSheet")
    @ApiOperation("查询流程实例详情")
    public FlowSheet getFlowSheet(
            @QueryParam("id") String id
    ) {
        return flowSheetService.getFlowSheetById(id);
    }

    @GET
    @Path("/approve")
    @ApiOperation("审批通过")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response approve(
            @QueryParam("id") String id,
            @QueryParam("auditorId") String auditorId,
            @QueryParam("auditorName") String auditorName,
            @QueryParam("comment") String comment
    ) {
        flowSheetService.approve(id, auditorId,auditorName, comment);
        return successUpdate();
    }

    @GET
    @Path("/reject")
    @ApiOperation("审批拒绝")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response reject(
            @QueryParam("id") String id,
            @QueryParam("auditorId") String auditorId,
            @QueryParam("auditorName") String auditorName,
            @QueryParam("comment") String comment
    ) {
        flowSheetService.reject(id, auditorId,auditorName, comment);
        return successUpdate();
    }

    @GET
    @Path("/cancel")
    @ApiOperation("审批撤销")
    public Response cancel(
            @QueryParam("id") String id
    ) {
        flowSheetService.cancel(id);
        return successUpdate();
    }

    @POST
    @Path("/reApply")
    @ApiOperation("重新申请")
    public Response reApply(
            @QueryParam("id") String id,
            @NotNull FlowSheetBO sheet
    ) {
        flowSheetService.reApply(id,sheet);
        return successUpdate();
    }
}
