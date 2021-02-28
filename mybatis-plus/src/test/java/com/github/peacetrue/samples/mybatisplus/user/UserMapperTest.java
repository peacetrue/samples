package com.github.peacetrue.samples.mybatisplus.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author : xiayx
 * @since : 2021-02-26 16:21
 **/
@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @Test
    public void selectList() {
        List<User> userList = userMapper.selectList(null);
        Assertions.assertEquals(5, userList.size());
    }

    @Test
    public void insertBatch() {
        List<User> users = IntStream.range(0, 100).boxed().map((i) -> new User("name", 18, "peace@a.com")).collect(Collectors.toList());
        userService.saveBatch(users, 50);
        Assertions.assertTrue(true);
    }
}
