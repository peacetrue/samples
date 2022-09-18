package com.github.peacetrue.sample.echo;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 回声服务实现自动配置。
 *
 * @author peace
 **/
@AutoConfigureOrder(10)
@Configuration
public class EchoServiceReactorAutoConfiguration {

    /**
     * 构造回声服务实现。
     *
     * @return 回声服务实现
     */
    @Bean
    @ConditionalOnMissingBean
    public EchoServiceReactorImpl echoService() {
        return new EchoServiceReactorImpl();
    }

}
