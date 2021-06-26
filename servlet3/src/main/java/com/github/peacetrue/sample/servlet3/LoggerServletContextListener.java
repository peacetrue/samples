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
public class LoggerServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("容器被初始");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("容器被销毁");
    }
}
