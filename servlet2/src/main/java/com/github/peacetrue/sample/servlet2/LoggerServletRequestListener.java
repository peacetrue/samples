package com.github.peacetrue.sample.servlet3;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * @author : xiayx
 * @since : 2021-06-26 18:31
 **/
@Slf4j
public class LoggerServletRequestListener implements ServletRequestListener {
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        log.info("请求被销毁");
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        log.info("请求被初始");
    }
}
