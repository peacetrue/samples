package com.github.peacetrue.sample.echo;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 回声服务控制器。
 *
 * @author peace
 **/
@ResponseBody
@RequestMapping("/echo")
@AllArgsConstructor
public class EchoController {

    private EchoService echoService;

    @GetMapping
    public String echo(String input) {
        return echoService.echo(input);
    }

}
