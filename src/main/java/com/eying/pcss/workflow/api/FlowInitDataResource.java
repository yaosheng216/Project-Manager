package com.eying.pcss.workflow.api;

import com.eying.pcss.core.api.AbstractResource;
import com.eying.pcss.workflow.service.FlowInitDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 企业审批初始化管理接口
 */
@Component
@Path("/initData")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "企业审批初始化管理", produces = "application/json")
public class FlowInitDataResource extends AbstractResource {

    private final FlowInitDataService initDataService;

    @Autowired
    public FlowInitDataResource(FlowInitDataService initDataService) {
        this.initDataService = initDataService;
    }

    @GET
    @Path("/initCompanyWorkFlow")
    @ApiOperation("初始化企业审批流程数据")
    public Response createGroup(
            @QueryParam("companyId") String companyId
    ){
        initDataService.initData(companyId);
        return this.successCreate();
    }

}
