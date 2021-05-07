package com.github.peacetrue.samples.elasticsearch.user;

import com.github.peacetrue.spring.data.domain.PageableUtils;
import com.github.peacetrue.user.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.io.IOException;
import java.util.List;

/**
 * @author : xiayx
 * @since : 2021-03-12 19:42
 **/
@Slf4j
@SpringBootTest(classes = {
        ElasticsearchRestClientAutoConfiguration.class,
        ElasticsearchDataAutoConfiguration.class,
        UserServiceImpl.class
})
public class UserServiceImplTest {

    public static final UserAdd ADD = UserAdd.builder().username("admin").password("admin").build();
    public static final UserModify MODIFY = UserModify.builder().id("1").username("admin").build();
    public static UserVO vo;

    static {
        ADD.setOperatorId(1L);
    }

    @Autowired
    private UserService userService;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    void createMapping() {
        Document document = elasticsearchRestTemplate.indexOps(User.class).createMapping();
        log.info("document: {}", document);
    }

    @Test
    void getIndexCoordinatesFor() throws IOException {
        IndexCoordinates indexCoordinates = elasticsearchRestTemplate.getIndexCoordinatesFor(User.class);
        log.info("IndexCoordinates: {}", indexCoordinates);
    }


    @Test
    void add() {
        UserVO vo = userService.add(ADD);
        Assertions.assertNotNull(vo);
        log.info("vo: {}", vo);
    }

    @Test
    void query() {
        UserQuery userQuery = UserQuery.builder().username("admin").build();
        Page<UserVO> page = userService.query(userQuery, PageableUtils.PAGEABLE_DEFAULT);
        Assertions.assertNotNull(page.getContent());
        log.info("page: {}", page.getContent());
    }

    @Test
    void get() {
        List<UserVO> vos = userService.query(UserQuery.DEFAULT);
        UserVO vo = userService.get(new UserGet(vos.get(0).getId()));
        log.info("vo: {}", vo);
    }

    @Test
    void modify() {
        List<UserVO> vos = userService.query(UserQuery.DEFAULT);
        UserVO vo = vos.get(0);
        Integer count = userService.modify(new UserModify(vo.getId(), "admin-02", vo.getCreatedTime()));
        Assertions.assertTrue(count > 0);
    }

    @Test
    void delete() {
        List<UserVO> vos = userService.query(UserQuery.DEFAULT);
        UserVO vo = vos.get(0);
        Integer count = userService.delete(new UserDelete(vo.getId(), vo.getCreatedTime()));
        Assertions.assertTrue(count > 0);
    }
}
