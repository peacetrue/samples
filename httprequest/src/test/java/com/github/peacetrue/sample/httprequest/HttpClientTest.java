package com.github.peacetrue.samples.httprequest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 服务端状态码 500 ，仍可向客户端输出响应数据，
 * 但各种客户端在接收到服务端 500 状态码之后，都抛出异常，不再解析后续数据
 *
 * @author : xiayx
 * @since : 2020-12-29 14:59
 **/
@Slf4j
class HttpClientTest {

    @Test
    void httpURLConnection() throws Exception {
        log.info("测试 HttpURLConnection 在收到 500 错误之后，抛出异常");
        HttpServerUtils.start();

        URL url = new URL("http://localhost:8001/test");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            connection.getInputStream();
            Assertions.fail("应该抛出异常");
        } catch (IOException e) {
            log.warn("抛出异常，并且无法获取返回结果", e);
        }
    }

    @Test
    void restTemplate() throws IOException {
        log.info("测试 RestTemplate 在收到 500 错误之后，抛出异常");
        HttpServerUtils.start();
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getForEntity("http://localhost:8001/test", String.class);
            Assertions.fail("应该抛出异常");
        } catch (RestClientException e) {
            log.warn("抛出异常，并且无法获取返回结果", e);
        }
    }

    @Test
    void webClient() throws IOException {
        log.info("测试 WebClient 在收到 500 错误之后，抛出异常");
        HttpServerUtils.start();
        try {
            WebClient.create("http://localhost:8001")
                    .get()
                    .uri("/test")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            Assertions.fail("应该抛出异常");
        } catch (WebClientResponseException e) {
            log.warn("抛出异常，并且无法获取返回结果", e);
        }
    }

}
