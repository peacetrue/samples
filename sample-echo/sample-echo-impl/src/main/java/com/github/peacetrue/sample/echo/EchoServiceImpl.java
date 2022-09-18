package com.github.peacetrue.sample.echo;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 回声服务实现。
 *
 * @author peace
 **/
@Slf4j
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(@Nullable String input) {
        log.info("echo: {}", input);
        return input == null ? UUID.randomUUID().toString() : input;
    }

    @Override
    public String echo(@Nullable String input, @Nullable Integer sleepTime) {
        if (sleepTime != null) {
            try {
                TimeUnit.MILLISECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                log.error("echo sleep error", e);
                Thread.currentThread().interrupt();
            }
        }
        return echo(input);
    }
}
