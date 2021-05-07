package com.github.peacetrue.elasticsearch.index.query;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.function.Function;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

/**
 * @author : xiayx
 * @since : 2021-03-13 09:23
 **/
public abstract class QueryBuilderUtils {

    protected QueryBuilderUtils() {
    }

    public static <T> QueryBuilder nullable(T value, Function<T, QueryBuilder> builderFactory) {
        return value == null ? null : builderFactory.apply(value);
    }

    public static BoolQueryBuilder build(BoolQueryBuilder boolQueryBuilder, QueryBuilder... builders) {
        for (QueryBuilder builder : builders) {
            if (builder != null) boolQueryBuilder.filter(builder);
        }
        return boolQueryBuilder;
    }

    public static BoolQueryBuilder build(QueryBuilder... builders) {
        return build(boolQuery(), builders);
    }
}
