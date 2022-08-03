package com.github.peacetrue.samples.https;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.ResourceAccessException;

import java.util.Objects;

/**
 * 抽象类，防止执行测试方法
 *
 * @author : xiayx
 * @since : 2021-08-21 06:02
 **/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class CertificateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    static void beforeAll() {
//        System.setProperty("javax.net.debug", "ssl,handshake");
    }

    protected String echo(String message) {
        return restTemplate.getForObject("/echo?message={0}", String.class, message);
    }

    protected void assertSuccess(String message) {
        String input = "ok";
        String result = echo(input);
        Assertions.assertEquals(input, result, message);
    }

    protected void assertThrows(String message) {
        ResourceAccessException exception = Assertions.assertThrows(
                ResourceAccessException.class,
                () -> echo("ex"),
                message
        );
        exception.printStackTrace();
        Assertions.assertTrue(Objects.requireNonNull(exception.getMessage()).contains(message));
    }
}
