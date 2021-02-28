package com.github.peacetrue.samples.mavenassembly;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : xiayx
 * @since : 2021-01-28 09:29
 **/
@Slf4j
@SpringBootApplication
public class MavenAssemblyApplication {
    public static void main(String[] args) {
        log.info("启动应用");
        SpringApplication.run(MavenAssemblyApplication.class, args);
    }
}
