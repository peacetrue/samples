package com.github.peacetrue.samples.hadoop;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.*;

/**
 * @author : xiayx
 * @since : 2020-10-27 11:28
 **/
@Slf4j
public class MySQLLargeTest {

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/common", "root", "12345678");
    }

    @Test
    void createTable() throws Exception {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        boolean execute = statement.execute("CREATE TABLE large_user\n" +
                "(\n" +
                "    id            BIGINT(20) auto_increment COMMENT '主键ID',\n" +
                "    name          VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',\n" +
                "    password      VARCHAR(30) NULL DEFAULT NULL COMMENT '密码',\n" +
                "    age           INT(11)     NULL DEFAULT NULL COMMENT '年龄',\n" +
                "    creator_id    BIGINT(20)  not null,\n" +
                "    created_time  datetime    not null,\n" +
                "    modifier_id   BIGINT(20)  not null,\n" +
                "    modified_time datetime    not null,\n" +
                "    PRIMARY KEY (id)\n" +
                ")");
        log.info("CREATE TABLE large_user: {}", execute);
    }

    @Test
    public void singleAutoCommit() throws Exception {
        int records = 10_000;
        Connection connection = getConnection();
        connection.setAutoCommit(true);

        PreparedStatement statement = connection.prepareStatement("insert into large_user" +
                " (NAME, PASSWORD, CREATOR_ID, CREATED_TIME, MODIFIER_ID, MODIFIED_TIME)" +
                " values (?,?,?,?,?,?)");

        long start = System.currentTimeMillis();

        for (int index = 1; index < records; index++) {
            statement.setString(1, "name" + index);
            statement.setString(2, "password" + index);
            statement.setLong(3, index);
            statement.setDate(4, new Date(System.currentTimeMillis()));
            statement.setLong(5, index);
            statement.setDate(6, new Date(System.currentTimeMillis()));
            statement.executeUpdate();
        }

        long end = System.currentTimeMillis();
        log.info("total time taken = {} ms", end - start);
        log.info("avg time taken = {} ms", (end - start) / records);
        //13:34:36.360 [Test worker] INFO  c.g.p.learn.sql.MySQLLargeTest - total time taken = 56537 ms
        //13:34:36.363 [Test worker] INFO  c.g.p.learn.sql.MySQLLargeTest - avg time taken = 5 ms
        statement.close();
        connection.close();
    }

    @Test
    public void singleBatchCommit() throws Exception {
        int records = 10_000;
        Connection connection = getConnection();
        connection.setAutoCommit(false);

        PreparedStatement statement = connection.prepareStatement("insert into large_user" +
                " (NAME, PASSWORD, CREATOR_ID, CREATED_TIME, MODIFIER_ID, MODIFIED_TIME)" +
                " values (?,?,?,?,?,?)");

        long start = System.currentTimeMillis();

        for (int index = 1; index < 10; index++) {
            for (int j = 0; j < 1_000; j++) {
                statement.setString(1, "name" + index);
                statement.setString(2, "password" + index);
                statement.setLong(3, index);
                statement.setDate(4, new Date(System.currentTimeMillis()));
                statement.setLong(5, index);
                statement.setDate(6, new Date(System.currentTimeMillis()));
                statement.executeUpdate();
            }
            connection.commit();
        }

        long end = System.currentTimeMillis();
        log.info("total time taken = {} ms", end - start);
        log.info("avg time taken = {} ms", (end - start) / records);
        //13:37:57.976 [Test worker] INFO  c.g.p.learn.sql.MySQLLargeTest - total time taken = 36384 ms
        //13:37:57.981 [Test worker] INFO  c.g.p.learn.sql.MySQLLargeTest - avg time taken = 3 ms
        statement.close();
        connection.close();
    }

    @Test
    public void batchAutoCommit() throws Exception {
        Connection connection = getConnection();
        connection.setAutoCommit(true);

        PreparedStatement preparedStatement = connection.prepareStatement("insert into large_user" +
                " (NAME, PASSWORD, CREATOR_ID, CREATED_TIME, MODIFIER_ID, MODIFIED_TIME)" +
                " values (?,?,?,?,?,?)");

        long start = System.currentTimeMillis();

        for (int i = 0; i < 10; i++) {
            batch(preparedStatement, 1000);
        }
        long end = System.currentTimeMillis();
        log.info("total time taken = {} ms", end - start);
        log.info("avg time taken = {} ms", (end - start) / 10_000);
        //13:39:21.795 [Test worker] INFO  c.g.p.learn.sql.MySQLLargeTest - total time taken = 38452 ms
        //13:39:21.798 [Test worker] INFO  c.g.p.learn.sql.MySQLLargeTest - avg time taken = 3 ms
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void batchCommit() throws Exception {
        Connection connection = getConnection();
        connection.setAutoCommit(false);

        PreparedStatement preparedStatement = connection.prepareStatement("insert into large_user" +
                " (NAME, PASSWORD, CREATOR_ID, CREATED_TIME, MODIFIER_ID, MODIFIED_TIME)" +
                " values (?,?,?,?,?,?)");

        long start = System.currentTimeMillis();
        int count = 1_000_000;
        for (int i = 0; i < count / 1_000; i++) {
            batch(preparedStatement, 1000);
            connection.commit();
        }
        long end = System.currentTimeMillis();
        log.info("total time taken = {} ms", end - start);

        log.info("avg time taken = {} ms", (end - start) / count);
        //13:02:51.760 [Test worker] INFO  c.g.p.learn.sql.MySQLLargeTest - total time taken = 28869 ms
        //13:02:51.768 [Test worker] INFO  c.g.p.learn.sql.MySQLLargeTest - avg time taken = 2 ms
        preparedStatement.close();
        connection.close();
    }

    private void batch(PreparedStatement preparedStatement, int count) throws SQLException {
        for (int index = 1; index <= count; index++) {
            preparedStatement.setString(1, "name" + index);
            preparedStatement.setString(2, "password" + index);
            preparedStatement.setLong(3, index);
            preparedStatement.setDate(4, new Date(System.currentTimeMillis()));
            preparedStatement.setLong(5, index);
            preparedStatement.setDate(6, new Date(System.currentTimeMillis()));
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
    }


}
