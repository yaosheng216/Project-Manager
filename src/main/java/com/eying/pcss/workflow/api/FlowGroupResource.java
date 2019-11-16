package com.eying.pcss.workflow.api;

import com.eying.pcss.core.api.AbstractResource;
import com.eying.pcss.workflow.entity.FlowGroup;
import com.eying.pcss.workflow.service.FlowGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * 审批应用分组管理接口
 */
@Component
@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "审批应用分组管理", produces = "application/json")
public class FlowGroupResource extends AbstractResource {

    private final FlowGroupService flowGroupService;

    @Autowired
    public FlowGroupResource(FlowGroupService flowGroupService) {
        this.flowGroupService = flowGroupService;
    }

    @GET
    @Path("/getGroupList")
    @ApiOperation("查询流程分组列表")
    public List<FlowGroup> getGroups(
            @QueryParam("companyId") String companyId
    ) {
        return flowGroupService.findAll (companyId);
    }

    @GET
    @Path("/getFlowGroup")
    @ApiOperation("查询应用分组详情")
    public FlowGroup getFlowGroup(
            @QueryParam("id") Long id
    ) {
        return flowGroupService.getFlowGroup (id);
    }

    @GET
    @Path("/createGroup")
    @ApiOperation("创建流程分组")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createGroup(
            @QueryParam("name") String name,
            @QueryParam("companyId") String companyId
    ) {
        return this.successCreate (flowGroupService.saveFlowGroup (name, companyId));
    }

    @GET
    @Path("/modifyGroupName")
    @ApiOperation("修改流程分组名称")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response modifyGroupName(
            @QueryParam("id") Long id,
            @QueryParam("name") String name
    ) {
        flowGroupService.modifyGroupName (id, name);
        return this.successUpdate ();
    }

    @GET
    @Path("/deleteFlowGroup")
    @ApiOperation("删除应用分组")
    public Response deleteFlowGroup(
            @QueryParam("id") Long id
    ) {
        flowGroupService.deleteFlowGroupById (id);
        return this.successDelete ();
    }
}
