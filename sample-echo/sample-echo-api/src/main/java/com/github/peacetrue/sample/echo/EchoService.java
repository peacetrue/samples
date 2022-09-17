package com.github.peacetrue.sample.echo;

/**
 * 回声服务。
 *
 * @author peace
 **/
public interface EchoService {

    /**
     * 返回输入值。
     *
     * @param input 输入
     * @return 输出
     */
    String echo(String input);
}
