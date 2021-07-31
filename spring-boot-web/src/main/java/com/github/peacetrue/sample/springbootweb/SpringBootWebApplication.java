package com.github.peacetrue.sample.springbootweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author : xiayx
 * @since : 2021-06-17 15:59
 **/
@SpringBootApplication
public class SpringBootWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebApplication.class, args);
    }

    @Bean
    public Filter filter() {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
                Filter.super.init(filterConfig);
            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                chain.doFilter(request, response);
            }
        };
    }

    @Bean
    public GenericFilterBean filterBean(){
        return new GenericFilterBean() {
            @Override
            protected void initFilterBean() throws ServletException {
                super.initFilterBean();
            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                chain.doFilter(request, response);
            }
        };
    }

    @RestController
    public static class IndexController {
        @RequestMapping("/index")
        public String index() {
            return "index";
        }
    }
}
