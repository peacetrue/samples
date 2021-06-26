package com.github.peacetrue.sample.servlet3;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * @author : xiayx
 * @since : 2021-06-26 18:19
 **/
@Slf4j
public class LoggerHttpSessionAttributeListener implements HttpSessionAttributeListener {
    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
        log.info("属性[{}={}]已被添加到会话", se.getName(), se.getValue());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {
        log.info("属性[{}={}]已从会话中移除", se.getName(), se.getValue());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {
        log.info("属性[{}]被替换：{} -> {}", se.getName(), se.getValue(), se.getSession().getAttribute(se.getName()));
    }
}
