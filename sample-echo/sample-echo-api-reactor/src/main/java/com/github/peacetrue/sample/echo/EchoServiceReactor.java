package com.github.peacetrue.sample.echo;

import reactor.core.publisher.Mono;

import javax.annotation.Nullable;

/**
 * 回声服务。
 *
 * @author peace
 **/
public interface EchoServiceReactor {

    /**
     * 返回输入值，{@code null} 时，随机生成一个值。
     *
     * @param input 输入值
     * @return 输出值
     */
    Mono<String> echo(@Nullable String input);
}
