package com.github.peacetrue.sample.echo;

import javax.annotation.Nullable;

/**
 * 回声服务。
 *
 * @author peace
 **/
public interface EchoService {

    /**
     * 返回输入值，{@code null} 时，随机生成一个值。
     *
     * @param input 输入值
     * @return 输出值
     */
    default String echo(@Nullable String input) {
        return echo(input, null);
    }

    /**
     * 返回输入值，{@code null} 时，随机生成一个值。
     *
     * @param input     输入值
     * @param sleepTime 睡眠时间
     * @return 输出值
     */
    String echo(@Nullable String input, @Nullable Integer sleepTime);
}
