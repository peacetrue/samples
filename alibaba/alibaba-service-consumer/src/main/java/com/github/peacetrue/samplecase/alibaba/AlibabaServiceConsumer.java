package com.github.peacetrue.samplecase.alibaba;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

/**
 * @author : xiayx
 * @since : 2021-01-28 09:29
 **/
@Slf4j
@SpringBootApplication
public class AlibabaServiceConsumer {

    public static void main(String[] args) {
        log.info("启动应用");
        SpringApplication.run(AlibabaServiceConsumer.class, args);
        // http://127.0.0.1:8848/nacos
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @FeignClient(name = "service-provider")
    public interface EchoService {
        @GetMapping(value = "/echo/{str}")
        String echo(@PathVariable("str") String str);
    }
}
