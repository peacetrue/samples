package com.github.peacetrue.samples.httprequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EmployeeControllerTest.class)
@WebFluxTest(controllers = RestExample.class)
public class EmployeeControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    void testCreateEmployee() {
        webClient.post()
                .uri("/test-not-null")
                .exchange()
                .expectStatus()
                .isCreated()
        ;
    }


}
