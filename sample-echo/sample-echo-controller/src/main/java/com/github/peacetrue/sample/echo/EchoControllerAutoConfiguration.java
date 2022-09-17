package com.github.peacetrue.sample.echo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 回声服务实现自动配置。
 *
 * @author peace
 **/
@Configuration
public class EchoControllerAutoConfiguration {

    /**
     * 构造回声服务控制器。
     *
     * @param echoService 回声服务
     * @return 回声服务控制器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(EchoService.class)
    public EchoController echoController(EchoService echoService) {
        return new EchoController(echoService);
    }

}
