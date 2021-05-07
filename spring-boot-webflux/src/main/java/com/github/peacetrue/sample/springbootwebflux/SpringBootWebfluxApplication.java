package com.github.peacetrue.sample.springbootwebflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : xiayx
 * @since : 2021-05-07 10:45
 **/
@SpringBootApplication
public class SpringBootWebfluxApplication {
    public static void main(String[] args) {
        // http://localhost:8080/actuator
        // http://localhost:8080/actuator/health
        SpringApplication.run(SpringBootWebfluxApplication.class, args);
    }
}
