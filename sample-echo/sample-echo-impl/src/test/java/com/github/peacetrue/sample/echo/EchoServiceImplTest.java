package com.github.peacetrue.sample.echo;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author peace
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EchoServiceAutoConfiguration.class)
class EchoServiceImplTest {

    @Autowired
    private EchoService echoService;

    @Test
    void echo() {
        String input = RandomStringUtils.random(10);
        Assertions.assertEquals(input, echoService.echo(input));
    }
}
