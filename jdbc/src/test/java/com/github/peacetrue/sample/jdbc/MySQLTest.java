package com.github.peacetrue.sample.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * @author : xiayx
 * @since : 2020-10-11 09:52
 **/
@Slf4j
public class MySQLTest {

    private Connection connection;
    public Statement statement;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/common", "root", "12345678");
        statement = connection.createStatement();
        statement.execute("drop table if exists test_blob");
        statement.execute("create table test_blob" +
                "(" +
                "    id       bigint primary key auto_increment," +
                "    value    blob" +
                ")");
    }

    @AfterEach
    void tearDown() throws Exception {
        statement.close();
        connection.close();
    }

    @Test
    void statement() throws Exception {
        statement.execute(String.format("insert into test_blob(value) values (0b%s)", "11"));
        statement.execute(String.format("insert into test_blob(value) values (0x%s)", "3"));
        statement.execute(String.format("insert into test_blob(value) values ('%s')", "0"));
        statement.execute(String.format("insert into test_blob(value) values (_binary'%s')", "11"));
    }

    @Test
    void preparedStatement() throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into test_blob(value) values (?)");
        preparedStatement.setBlob(1, this.getClass().getResourceAsStream("/value_1"));
        preparedStatement.execute();
        preparedStatement.close();
    }
}
