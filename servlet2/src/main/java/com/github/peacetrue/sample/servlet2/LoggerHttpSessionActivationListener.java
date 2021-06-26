package com.github.peacetrue.sample.servlet3;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;

/**
 * @author : xiayx
 * @since : 2021-06-26 18:17
 **/
@Slf4j
public class LoggerHttpSessionActivationListener implements HttpSessionActivationListener {
    @Override
    public void sessionWillPassivate(HttpSessionEvent se) {
        log.info("会话将要过期");
    }

    @Override
    public void sessionDidActivate(HttpSessionEvent se) {
        log.info("会话已被激活");
    }
}
