package com.github.peacetrue.sample.servlet3;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * @author : xiayx
 * @since : 2021-06-26 18:26
 **/
@Slf4j
public class LoggerHttpSessionBindingListener implements HttpSessionBindingListener {
    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        log.info("值[{}]被绑定", event.getName());
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        log.info("值[{}]被解除", event.getName());
    }
}
