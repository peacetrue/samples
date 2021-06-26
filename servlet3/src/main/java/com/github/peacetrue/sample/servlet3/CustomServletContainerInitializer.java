package com.github.peacetrue.sample.servlet3;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author : xiayx
 * @since : 2021-06-27 05:16
 **/
public interface CustomServletContainerInitializer {
    void onStartup(ServletContext ctx) throws ServletException;
}
