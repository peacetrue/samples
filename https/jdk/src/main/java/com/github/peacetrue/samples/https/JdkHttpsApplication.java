package com.github.peacetrue.samples.https;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : xiayx
 * @since : 2021-06-17 15:59
 **/
@SpringBootApplication
public class JdkHttpsApplication {

    public static void main(String[] args) {
        System.setProperty("javax.net.debug", "ssl,handshake,trustmanager");
//        System.setProperty("javax.net.ssl.trustStore", "/Users/xiayx/Documents/Projects/sample-https/sample-https-tomcat/src/main/resources/localhost.jks");
//        System.setProperty("javax.net.ssl.trustStorePassword", "peacetrue");
        SpringApplication.run(JdkHttpsApplication.class, args);
    }

}
