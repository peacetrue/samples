package com.github.peacetrue.sample.servlet3;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author : xiayx
 * @since : 2021-06-27 05:20
 **/
@Slf4j
public class LoggerCustomServletContainerInitializer implements CustomServletContainerInitializer {
    @Override
    public void onStartup(ServletContext ctx) throws ServletException {
        log.info("服务启动");
    }
}
