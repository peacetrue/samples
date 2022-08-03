package com.github.peacetrue.runnable_jar;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SpringBootWebApplicationTest {

    private final RestTemplate restTemplate = new RestTemplate();
    private MultiValueMap<String, String> params = getStringStringMultiValueMap();
    private HttpHeaders headers = getHttpHeaders();

    public static void main(String[] args) throws Exception {
        SpringBootWebApplicationTest application = new SpringBootWebApplicationTest();
//        System.out.println(application.getAccessToken());

        List<Thread> threads = IntStream.range(0, 5)
                .mapToObj(i -> new Thread(application::getAccessTokens))
                .collect(Collectors.toList());
        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }
    }

    private void getAccessTokens() {
        while (true) {
            try {
                getAccessToken();
            } catch (Exception ignored) {
                //ignored.printStackTrace();
            }
        }
    }

    private String getAccessToken() {
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        return restTemplate.postForObject("https://bqpdvxseta.api.aliyunidaas.com/oauth/token", request, String.class);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        return headers;
    }

    private MultiValueMap<String, String> getStringStringMultiValueMap() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(4);
        params.add("client_id", "48e166b4848c456f8a9c63dec4cab126BZ86zS5gyi4");
        params.add("client_secret", "QEyYOH6fDN4e7Hwbgl39klDNrYhS0eQYnXpvdsVpmI");
        params.add("grant_type", "client_credentials");
        params.add("scope", "read");
        return params;
    }
}
