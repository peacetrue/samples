package com.github.peacetrue.sample.servlet3;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.EnumSet;

/**
 * @author : xiayx
 * @since : 2021-06-26 18:13
 **/
@Slf4j
@WebListener
public class DynamicServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("动态注册");
        //如果给定的过滤器映射应该在任何声明的过滤器映射之后匹配，则为 true，
        //如果它应该在获得此 FilterRegistration 的 ServletContext 的任何已声明的过滤器映射之前匹配，则为 false
        String filterName = DynamicFilter.class.getName();
        servletContextEvent.getServletContext()
                .addFilter(filterName + "After", new DynamicFilter())
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
        servletContextEvent.getServletContext()
                .addFilter(filterName + "Before", new DynamicFilter())
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("容器被销毁");
    }
}
