package com.github.peacetrue.samplecase.alibaba;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : xiayx
 * @since : 2021-01-28 09:29
 **/
@Slf4j
@SpringBootApplication
public class AlibabaServiceProvider {

    public static void main(String[] args) {
        log.info("启动应用");
        SpringApplication.run(AlibabaServiceProvider.class, args);
        // http://127.0.0.1:8848/nacos
    }

    @RestController
    private static class EchoController {
        @GetMapping(value = "/echo/{string}")
        public String echo(@PathVariable String string) {
            return string;
        }
    }
}
