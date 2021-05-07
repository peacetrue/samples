package com.github.peacetrue.sample.hive;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author : xiayx
 * @since : 2021-03-24 18:37
 **/
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HiveTest {

    @Test
    @Order(0)
    void drop() throws Exception {
        Statement statement = getStatement();
        //TODO 存在表并且确实删除了，也是返回 false
        boolean result = statement.execute("drop table if exists students");
        log.debug("result: {}", result);
        Assertions.assertTrue(true);
        List<Map<String, Object>> items = query(statement);
        Assertions.assertEquals(0, items.size());
    }

    @Test
    @Order(10)
    void create() throws Exception {
        Statement statement = getStatement();
        boolean result = statement.execute("create table students(  \n" +
                "  id int,  \n" +
                "  name string\n" +
                ")");
        log.debug("result: {}", result);
        List<Map<String, Object>> items = query(statement);
        Assertions.assertEquals(0, items.size());
    }

    @Test
    @Order(20)
    void insert() throws Exception {
        Statement statement = getStatement();
        statement.execute("insert into students values(1,'小红')");
        List<Map<String, Object>> items = query(statement);
        Assertions.assertEquals(1, items.size());
    }

    private Statement getStatement() throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        Connection connection = DriverManager.getConnection("jdbc:hive2://hive-node01:10000/default", "root", "123456");
        return connection.createStatement();
    }

    private List<Map<String, Object>> query(Statement statement) throws SQLException {
        List<Map<String, Object>> items = new LinkedList<>();
        ResultSet resultSet = statement.executeQuery("select * from students");
        while (resultSet.next()) {
            Map<String, Object> item = new LinkedHashMap<>(2);
            item.put("id", resultSet.getInt("id"));
            item.put("value", resultSet.getString("value"));
            items.add(item);
        }
        return items;
    }

    @Test
    void transaction() throws Exception {
        Statement statement = getStatement();

        statement.execute("update students set value='小红1' where id = 1");
        List<Map<String, Object>> items = query(statement);
        Assertions.assertEquals(1, items.size());

        statement.execute("delete from students where id = 1");
        items = query(statement);
        Assertions.assertEquals(0, items.size());
    }

    @Test
    void metaStoreClient() throws MetaException {
        HiveConf hiveConf = new HiveConf(this.getClass());
        hiveConf.addResource("hive-site.xml");
        HiveMetaStoreClient client = new HiveMetaStoreClient(hiveConf);
        List<String> tables = client.getAllTables("default");
        Assertions.assertEquals(0, tables.size());
    }
}
