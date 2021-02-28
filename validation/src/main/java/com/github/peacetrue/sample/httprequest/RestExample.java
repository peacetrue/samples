package com.github.peacetrue.samples.httprequest;

import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
public class RestExample {

    @PostMapping("/test-not-null")
    public void testNotNull(@Valid @RequestBody NotNullRequest request) {
        // ...
    }

    @Data
    public static class NotNullRequest {

        @NotNull(message = "stringValue has to be present")
        private String stringValue;

        // getters, setters
    }
}
