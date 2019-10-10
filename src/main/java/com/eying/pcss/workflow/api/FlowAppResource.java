package com.eying.pcss.workflow.api;

import com.eying.pcss.core.api.AbstractResource;
import com.eying.pcss.workflow.dto.FlowFormInput;
import com.eying.pcss.workflow.entity.WorkFlowApp;
import com.eying.pcss.workflow.bo.FlowDefinitionBO;
import com.eying.pcss.workflow.bo.WorkFlowAppBO;
import com.eying.pcss.workflow.service.WorkFlowAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * 审批应用管理接口
 */
@Component
@Path("/apps")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "审批应用管理", produces = "application/json")
public class FlowAppResource extends AbstractResource {

    private final WorkFlowAppService workFlowAppService;

    @Autowired
    public FlowAppResource(WorkFlowAppService workFlowAppService) {
        this.workFlowAppService = workFlowAppService;
    }

    @GET
    @Path("/getAppList")
    @ApiOperation("查询应用列表")
    public List<WorkFlowApp> getApps(
            @QueryParam("companyId") String companyId,
            @QueryParam("name") String name,
            @QueryParam("groupId") String groupId
    ) {
        return workFlowAppService.getApps(companyId, name,groupId);
    }

    @POST
    @Path("/createApp")
    @ApiOperation("创建审批应用")
    public Response createGroupApp(
            @NotNull @Valid WorkFlowAppBO app
    ) {
        return this.successCreate(workFlowAppService.createGroupApp(app));
    }

    @GET
    @Path("/getAppDetail")
    @ApiOperation("查询应用详情")
    public WorkFlowApp getApp(
            @QueryParam("id") String id
    ) {
        return workFlowAppService.getApp(id);
    }

    @POST
    @Path("/editApp")
    @ApiOperation("编辑应用")
    public Response editApp(
            @QueryParam("id") String id,
            @NotNull WorkFlowAppBO app
    ) {
        workFlowAppService.editApp(id,app);
        return this.successUpdate();
    }

    @GET
    @Path("/deleteApp")
    @ApiOperation("删除应用")
    public Response deleteApp(
            @QueryParam("id") String id
    ) {
        workFlowAppService.deleteApp(id);
        return this.successDelete();
    }

    @GET
    @Path("/disableApp")
    @ApiOperation("停用应用")
    public Response disableApp(
            @QueryParam("id") String id
    ) {
        workFlowAppService.disableApp(id);
        return this.successUpdate();
    }

    @GET
    @Path("/enableApp")
    @ApiOperation("启用应用")
    public Response enableApp(
            @QueryParam("id") String id
    ) {
        workFlowAppService.enableApp(id);
        return this.successUpdate();
    }

    @POST
    @Path("/editForm")
    @ApiOperation("编辑应用表单")
    public Response editAppForm(
            @QueryParam("id") String id,
            @NotNull List<FlowFormInput> inputs
    ) {
        workFlowAppService.editAppForm(id,inputs);
        return this.successUpdate();
    }

    @POST
    @Path("/editFlow")
    @ApiOperation("编辑应用流程")
    public Response editAppFlow(
            @QueryParam("id") String id,
            @NotNull FlowDefinitionBO flow
    ) {
        workFlowAppService.editAppFlow(id,flow);
        return this.successUpdate();
    }

    @GET
    @Path("/publish")
    @ApiOperation("发布应用")
    public Response publish(
            @QueryParam("id") String id
    ) {
        // 需要做数据校验
        workFlowAppService.publish(id);
        return this.successUpdate();
    }
}
