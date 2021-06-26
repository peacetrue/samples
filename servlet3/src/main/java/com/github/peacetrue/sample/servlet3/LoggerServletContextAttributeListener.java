package com.github.peacetrue.sample.servlet3;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.annotation.WebListener;

/**
 * @author : xiayx
 * @since : 2021-06-26 18:28
 **/
@Slf4j
@WebListener
public class LoggerServletContextAttributeListener implements ServletContextAttributeListener {
    @Override
    public void attributeAdded(ServletContextAttributeEvent scab) {
        log.info("属性[{}]被添加", scab.getName());
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent scab) {
        log.info("属性[{}]被移除", scab.getName());
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent scab) {
        log.info("属性[{}]被替换：{} -> {}", scab.getName(), scab.getValue(), scab.getServletContext().getAttribute(scab.getName()));
    }
}
