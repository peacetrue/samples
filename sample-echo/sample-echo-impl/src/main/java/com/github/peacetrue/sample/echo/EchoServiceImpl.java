package com.github.peacetrue.sample.echo;

import lombok.extern.slf4j.Slf4j;

/**
 * 回声服务实现。
 *
 * @author peace
 **/
@Slf4j
public class EchoServiceImpl implements EchoService {
    @Override
    public String echo(String input) {
        log.info("echo: {}", input);
        return input;
    }
}
