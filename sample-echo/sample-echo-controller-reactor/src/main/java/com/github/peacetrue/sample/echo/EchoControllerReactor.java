package com.github.peacetrue.sample.echo;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;

/**
 * 回声服务控制器。
 *
 * @author peace
 **/
@ResponseBody
@RequestMapping("/echo")
@AllArgsConstructor
public class EchoControllerReactor {

    private EchoServiceReactor echoServiceReactor;

    @GetMapping
    public Mono<String> echo(@Nullable String input) {
        return echoServiceReactor.echo(input);
    }

}
