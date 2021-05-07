package com.github.peacetrue.samples.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

/**
 * @author : xiayx
 * @since : 2021-02-27 12:30
 **/
@Slf4j
//@EnableElasticsearchRepositories
@SpringBootTest(classes = {
        ElasticsearchRestClientAutoConfiguration.class,
//        ElasticsearchRepositoriesAutoConfiguration.class,
        ElasticsearchDataAutoConfiguration.class,
})
public class ElasticSearchTest {

    @Autowired
    private ElasticsearchRestClientProperties properties;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    void basic() {
        Assertions.assertNotNull(properties.getUris());
    }

    @Test
    void indices() {
//        IndexCoordinates indexCoordinates = elasticsearchRestTemplate.indexOps(User.class);
//        System.out.println(indexCoordinates);
        //highLevelClient.indices()
        // Assertions.assertNotNull(response);
    }


}
