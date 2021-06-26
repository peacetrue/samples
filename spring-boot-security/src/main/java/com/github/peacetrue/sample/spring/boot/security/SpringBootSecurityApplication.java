package com.github.peacetrue.sample.spring.boot.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : xiayx
 * @since : 2021-06-17 15:59
 **/
@SpringBootApplication
public class SpringBootSecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityApplication.class, args);
    }

    @RestController
    public static class IndexController {
        @RequestMapping("/index")
        public String index() {
            return "index";
        }
    }
}
