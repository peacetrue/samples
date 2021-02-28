package com.github.peacetrue.samples.ks3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : xiayx
 * @since : 2021-01-28 09:29
 **/
@Slf4j
@SpringBootApplication
public class SpringBootMavenApplication {
    public static void main(String[] args) {
        log.info("启动应用");
        SpringApplication.run(SpringBootMavenApplication.class, args);
    }
}
