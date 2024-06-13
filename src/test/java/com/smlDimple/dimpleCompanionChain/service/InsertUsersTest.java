package com.smlDimple.dimpleCompanionChain.service;

import com.smlDimple.dimpleCompanionChain.mapper.UserMapper;
import com.smlDimple.dimpleCompanionChain.model.domain.User;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

/**
 * @Author: small-dimple
 **/
@SpringBootTest

public class InsertUsersTest {

    @Resource
    private UserMapper userMapper;


    @Test
    public void insertUser() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();


        final int INSERT_NUM = 1000;
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("假酒窝");
            user.setUserAccount("smallDimple");
            user.setAvatarUrl("https://small-dimple.top/wp-content/uploads/2024/05/1586f3f715584596b7e9e7d6ee33f0dc.png");
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setPhone("12312312312");
            user.setEmail("123@qq.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setOrderNum("112222");
            user.setTags("[]");


            userMapper.insert(user);
        }

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
