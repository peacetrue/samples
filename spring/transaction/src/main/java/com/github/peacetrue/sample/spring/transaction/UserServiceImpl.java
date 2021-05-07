package com.github.peacetrue.sample.spring.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Collections;

/**
 * @author : xiayx
 * @since : 2021-03-04 09:20
 **/
@Service
public class UserServiceImpl {

    private SimpleJdbcInsert simpleJdbcInsert;
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("user")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public User add(User user) {
        user.setId((Long) simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(user)));
        return user;
    }

    public User addWithError(User user) {
        this.add(user);
        throw new IllegalStateException("1111");
    }

    public User getById(Long id) {
        return this.jdbcTemplate.queryForObject(
                "select * from user where id=:id",
                Collections.singletonMap("id", id),
                new BeanPropertyRowMapper<>(User.class)
        );
    }

    public User getByIdForUpdate(Long id) {
        return this.jdbcTemplate.queryForObject(
                "select * from user where id=:id for update",
                Collections.singletonMap("id", id),
                new BeanPropertyRowMapper<>(User.class)
        );
    }

    public Long count() {
        return this.jdbcTemplate.queryForObject("select count(id) from user", Collections.emptyMap(), Long.class);
    }
}
