package com.github.peacetrue.samples.https;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : xiayx
 * @since : 2021-06-17 15:59
 **/
@SpringBootApplication
public class ClientHttpsApplication {

    public static void main(String[] args) {
        System.setProperty("javax.net.debug", "ssl,handshake");
        SpringApplication.run(ClientHttpsApplication.class, args);
    }

}
