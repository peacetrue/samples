package com.github.peacetrue.sample.echo;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

/**
 * @author peace
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EchoServiceReactorAutoConfiguration.class)
class EchoServiceReactorImplTest {

    @Autowired
    private EchoServiceReactor echoServiceReactor;

    @Test
    void echo() {
        StepVerifier.create(echoServiceReactor.echo(null))
                .expectNextCount(1)
                .expectComplete()
                .verify();
        String input = RandomStringUtils.random(10);
        StepVerifier.create(echoServiceReactor.echo(input))
                .expectNext(input)
                .expectComplete()
                .verify();
    }
}
