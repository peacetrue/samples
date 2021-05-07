package com.github.peacetrue.samples.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.UUID;

/**
 * @author : xiayx
 * @since : 2021-02-27 12:30
 **/
@Slf4j
//@EnableElasticsearchRepositories
//@EnableReactiveElasticsearchRepositories
@SpringBootTest(classes = {
//        ElasticsearchRestClientAutoConfiguration.class,
//        ElasticsearchRepositoriesAutoConfiguration.class,
        ReactiveElasticsearchRestClientAutoConfiguration.class,
//        ReactiveElasticsearchRepositoriesAutoConfiguration.class,
//        ElasticsearchDataAutoConfiguration.class,
})
public class ReactiveElasticSearchTest {

    @Autowired
    private ReactiveElasticsearchClient elasticsearchClient;
//    @Autowired
//    private ReactiveElasticsearchTemplate elasticsearchRestTemplate;


    @Test
    void basic() {
        Assertions.assertNotNull(elasticsearchClient);
    }

    @Test
    void indexes() {
        Mono<IndexResponse> response = elasticsearchClient.index(request ->
                request.index("spring-data")
                        .type("elasticsearch")
                        .id(UUID.randomUUID().toString())
                        .source(Collections.singletonMap("feature", "reactive-client"))
                        .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE)
        );
        StepVerifier
                .create(response)
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }
}
