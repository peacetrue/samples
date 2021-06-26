package com.github.peacetrue.sample.servlet3;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author : xiayx
 * @since : 2021-06-26 18:05
 **/
@Slf4j
@WebFilter(urlPatterns = "/*")
public class LoggerFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("初始: {}", filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("前置处理");
        filterChain.doFilter(servletRequest, servletResponse);
        log.info("后置处理");
    }

    @Override
    public void destroy() {
        log.info("销毁");
    }
}
