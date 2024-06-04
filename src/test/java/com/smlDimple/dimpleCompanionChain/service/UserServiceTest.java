package com.smlDimple.dimpleCompanionChain.service;

import com.smlDimple.dimpleCompanionChain.model.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: small-dimple
 **/

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void searchUsersByTags() {
        List<String> tags = Arrays.asList("java", "python");
        List<User> userList = userService.searchUsersByTags(tags);
        assertNotNull(userList);
    }
}