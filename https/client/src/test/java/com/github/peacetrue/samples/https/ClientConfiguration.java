package com.github.peacetrue.samples.https;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;

import javax.net.ssl.SSLContext;
import java.io.File;

/**
 * @author : xiayx
 * @since : 2021-08-24 07:28
 **/
@Configuration(proxyBeanMethods = false)
public class ClientConfiguration {

    @Bean
    public RestTemplateCustomizer restTemplateCustomizer() {
        HttpClient client = getHttpClient();
        return restTemplate -> restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
    }

    private static HttpClient getHttpClient() {
        try {
            String password = "peacetrue";
            File clientKey = ResourceUtils.getFile("classpath:client-key.jks");
            File clientTrust = ResourceUtils.getFile("classpath:client-trust.jks");
            SSLContext sslContext = SSLContextBuilder
                    .create()
                    .loadKeyMaterial(clientKey, password.toCharArray(), password.toCharArray())
                    .loadTrustMaterial(clientTrust, password.toCharArray())
                    .build();
            return HttpClients.custom()
                    .setSSLContext(sslContext)
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
