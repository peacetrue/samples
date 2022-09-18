package com.github.peacetrue.sample.echo;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebTestClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author peace
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        EchoServiceReactorAutoConfiguration.class,
        EchoControllerReactorAutoConfiguration.class,
        WebFluxAutoConfiguration.class,
        JacksonAutoConfiguration.class,
        WebTestClientAutoConfiguration.class,
})
class EchoControllerReactorTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void echo() {
        String input = RandomStringUtils.randomAlphanumeric(10);
        String response = webTestClient.get()
                .uri("/echo?input={0}", input)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody()
                .blockFirst();
        Assertions.assertEquals(input, response);
    }
}
