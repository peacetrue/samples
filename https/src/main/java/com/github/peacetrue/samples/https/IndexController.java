package com.github.peacetrue.samples.https;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : xiayx
 * @since : 2021-08-19 09:13
 **/
@Slf4j
@Controller
public class IndexController {

    @GetMapping({"/index", ""})
    public String index() {
        log.info("come to index");
        return "index";
    }

    @ResponseBody
    @GetMapping("/echo")
    public String echo(String message) {
        log.info("echo '{}'", message);
        return message;
    }
}
