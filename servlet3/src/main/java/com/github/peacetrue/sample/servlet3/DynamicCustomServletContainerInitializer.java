package com.github.peacetrue.sample.servlet3;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;

/**
 * @author : xiayx
 * @since : 2021-06-27 05:20
 **/
@Slf4j
public class DynamicCustomServletContainerInitializer implements CustomServletContainerInitializer {
    @Override
    public void onStartup(ServletContext ctx) throws ServletException {
        log.info("动态注册");
        //如果给定的过滤器映射应该在任何声明的过滤器映射之后匹配，则为 true，
        //如果它应该在获得此 FilterRegistration 的 ServletContext 的任何已声明的过滤器映射之前匹配，则为 false
        String filterName = DynamicFilter.class.getName();
        ctx
                .addFilter(filterName + "After", new DynamicFilter())
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
        ctx
                .addFilter(filterName + "Before", new DynamicFilter())
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
    }
}
