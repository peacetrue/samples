package com.github.peacetrue.sample.echo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 回声服务实现自动配置。
 *
 * @author peace
 **/
@Configuration
public class EchoServiceAutoConfiguration {

    /**
     * 构造回声服务实现。
     *
     * @return 回声服务实现
     */
    @Bean
    @ConditionalOnMissingBean
    public EchoServiceImpl echoService() {
        return new EchoServiceImpl();
    }

}
