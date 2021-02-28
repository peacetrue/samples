package com.github.peacetrue.samples.mybatisplus;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : xiayx
 * @since : 2021-01-28 09:29
 **/
@Slf4j
@SpringBootApplication
@MapperScan("com.github.peacetrue.samples.mybatisplus")
public class MybatisPlusApplication {
    public static void main(String[] args) {
        log.info("启动应用");
        SpringApplication.run(MybatisPlusApplication.class, args);
    }
}
