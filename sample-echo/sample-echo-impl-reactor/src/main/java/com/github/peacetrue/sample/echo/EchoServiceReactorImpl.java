package com.github.peacetrue.sample.echo;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * 回声服务实现。
 *
 * @author peace
 **/
@Slf4j
public class EchoServiceReactorImpl implements EchoServiceReactor {
    @Override
    public Mono<String> echo(@Nullable String input) {
        log.info("echo: {}", input);
        return Mono.just(input == null ? UUID.randomUUID().toString() : input);
    }
}
