package com.eying.pcss.workflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * 去除APPLICATION_FORM_URLENCODED请求的警告信息
 */
@Configuration
public class FilterConfig {

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new MyHiddenHttpMethodFilter ();
    }

    /**
     * 自定义过滤器
     */
    static class MyHiddenHttpMethodFilter extends HiddenHttpMethodFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {
            if ("POST".equals (request.getMethod ())
                    && MediaType.APPLICATION_FORM_URLENCODED.equals (request.getContentType ())) {
                //Skip this filter and call the next filter in the chain.
                filterChain.doFilter (request, response);
            } else {
                //Continue with processing this filter.
                super.doFilterInternal (request, response, filterChain);
            }
        }
    }
}
