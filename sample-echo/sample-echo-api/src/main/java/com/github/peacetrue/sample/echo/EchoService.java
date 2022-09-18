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
    String echo(@Nullable String input);
}
