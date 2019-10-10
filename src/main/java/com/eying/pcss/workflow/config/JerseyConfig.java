package com.eying.pcss.workflow.config;


import com.eying.pcss.core.filter.JwtFilter;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Array;

/**
 * Jersey配置类
 */
@Component
public class JerseyConfig extends ResourceConfig {

    @Value("${spring.jersey.application-path}")
    private String apiPath;

    public JerseyConfig() {
        register(LoggingFeature.class);
        //定义包含接口定义的包
        packages("com.eying.pcss");
        //支持文件上传
        register(MultiPartFeature.class);
        //jwt信息处理
        register(JwtFilter.class);
        register(Array.class);
    }

    @PostConstruct
    public void init() {
        this.configureSwagger();
    }


    private void configureSwagger() {
        // Available at localhost:port/swagger.json
        this.register(ApiListingResource.class);
        this.register(SwaggerSerializers.class);
        BeanConfig config = new BeanConfig();
        config.setConfigId("pcss-workflow");
        config.setTitle("企业审批流程管理");
        config.setVersion("v1");
        config.setContact("zhangyunyan");
        config.setSchemes(new String[] { "http"});
        config.setBasePath(apiPath);
        config.setResourcePackage("com.eying.pcss");
        config.setPrettyPrint(true);
        config.setScan(true);
    }
}
