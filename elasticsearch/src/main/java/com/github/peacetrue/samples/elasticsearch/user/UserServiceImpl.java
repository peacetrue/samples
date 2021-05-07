package com.github.peacetrue.samples.elasticsearch.user;

import com.github.peacetrue.core.Operators;
import com.github.peacetrue.elasticsearch.index.query.QueryBuilderUtils;
import com.github.peacetrue.spring.util.BeanUtils;
import com.github.peacetrue.user.*;
import com.github.peacetrue.util.DateTimeFormatterUtils;
import com.github.peacetrue.util.DateUtils;
import com.github.peacetrue.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author : xiayx
 * @since : 2021-03-08 22:17
 **/
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    private final IndexCoordinates indexCoordinates = IndexCoordinates.of("user-*");

    @Override
    public UserVO add(UserAdd params) {
        log.info("新增用户[{}]", params);
        User user = Operators.setCreateModify(params, BeanUtils.map(params, User.class));
        user.setId(UUIDUtils.randomUUID());
        IndexCoordinates coordinates = elasticsearchRestTemplate.getIndexCoordinatesFor(User.class);
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(coordinates);
        if (!indexOperations.exists() && indexOperations.create()) {
            indexOperations.putMapping(indexOperations.createMapping(User.class));
        }
        return BeanUtils.map(elasticsearchRestTemplate.save(user), UserVO.class);
    }

    @Override
    public Page<UserVO> query(UserQuery params, Pageable pageable, String... projection) {
        log.info("分页查询用户[{}]", params);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFields(projection)
                .withQuery(getQueryBuilder(params))
                .withPageable(pageable)
                .build();
        SearchHits<User> searchHits = elasticsearchRestTemplate.search(searchQuery, User.class, indexCoordinates);
        List<UserVO> vos = searchHits.stream().map(SearchHit::getContent)
                .map(entity -> BeanUtils.map(entity, UserVO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(vos, pageable, searchHits.getTotalHits());
    }

    private BoolQueryBuilder getQueryBuilder(UserQuery params) {
        return QueryBuilderUtils.build(
                QueryBuilderUtils.nullable(params.getId(), value -> termsQuery("id", value)),
                QueryBuilderUtils.nullable(params.getUsername(), value -> termQuery("username", value)),
                QueryBuilderUtils.nullable(params.getCreatorId(), value -> termQuery("creatorId", value)),
                QueryBuilderUtils.nullable(params.getCreatedTime().getLowerBound(), value -> rangeQuery("createdTime").from(value)),
                QueryBuilderUtils.nullable(params.getCreatedTime().getUpperBound(), value -> rangeQuery("createdTime").to(DateUtils.DATE_CELL_EXCLUDE.apply(value)))
        );
    }

    @Override
    public List<UserVO> query(UserQuery params, String... projection) {
        return query(params, Sort.by("createdTime"), projection);
    }

    @Override
    public List<UserVO> query(UserQuery params, Sort sort, String... projection) {
        log.info("全量查询用户[{}]", params);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFields(projection)
                .withPageable(PageRequest.of(0, 10000, sort))
                .withFilter(getQueryBuilder(params))
                .build();
        SearchHits<User> searchHits = elasticsearchRestTemplate.search(searchQuery, User.class, indexCoordinates);
        return searchHits.stream().map(SearchHit::getContent)
                .map(entity -> BeanUtils.map(entity, UserVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserVO get(UserGet params, String... projection) {
        log.info("获取用户信息[{}]", params);
        SearchHit<User> searchHit = getSearchHit(params, projection);
        if (searchHit == null) return null;
        return BeanUtils.map(searchHit.getContent(), UserVO.class);
    }

    private SearchHit<User> getSearchHit(UserGet params, String... projection) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withFields(projection)
                .withFilter(QueryBuilders.termQuery("id", params.getId()));
        return elasticsearchRestTemplate.searchOne(queryBuilder.build(), User.class, indexCoordinates);
    }

    @Override
    public Integer modify(UserModify params) {
        log.info("修改用户信息[{}]", params);
        UpdateQuery updateQuery = UpdateQuery.builder(params.getId())
                .withDocument(Document.from(Collections.singletonMap("username", params.getUsername())))
                .build();
        IndexCoordinates coordinates = getIndexCoordinates(params.getCreatedTime());
        UpdateResponse response = elasticsearchRestTemplate.update(updateQuery, coordinates);
        return response.getResult() == UpdateResponse.Result.UPDATED ? 1 : 0;
    }

    private IndexCoordinates getIndexCoordinates(LocalDateTime createdTime) {
        String indexName = "user-" + createdTime.format(DateTimeFormatterUtils.SHORT_DATE_TIME);
        return IndexCoordinates.of(indexName);
    }

    @Override
    public Boolean exists(UserGet userGet) {
        return this.getSearchHit(userGet) != null;
    }

    @Override
    public Integer modifyPassword(UserModifyPassword userModifyPassword) {
        return null;
    }

    @Override
    public Integer resetPassword(UserResetPassword userResetPassword) {
        return null;
    }

    @Override
    public Integer delete(UserDelete params) {
        log.info("删除用户信息[{}]", params);
        IndexCoordinates coordinates = getIndexCoordinates(params.getCreatedTime());
        //返回 id
        String response = elasticsearchRestTemplate.delete(params.getId(), coordinates);
        log.info("删除用户信息返回[{}]", response);
        return response == null ? 0 : 1;
    }
}
