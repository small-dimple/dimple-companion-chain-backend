package com.smlDimple.dimpleCompanionChain.service;

import com.smlDimple.dimpleCompanionChain.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

/**
 * @Author: small-dimple
 **/
@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;


    @Test
    public void test(){

        ValueOperations<String, Object> stringObjectValueOperations = redisTemplate.opsForValue();

        //redis的增加操作
        stringObjectValueOperations.set("test","dimple");
        stringObjectValueOperations.set("test1",3);
        stringObjectValueOperations.set("test2",2.0);
        User user = new User();
        user.setId(1L);
        user.setUsername("dimple");
        stringObjectValueOperations.set("test3",user);
        Object test = stringObjectValueOperations.get("test");
        //redis的查询操作
        Object dimple = stringObjectValueOperations.get("test");
        Assertions.assertTrue("dimple".equals(dimple));
        Object test1 = stringObjectValueOperations.get("test1");
        Assertions.assertTrue(3 == (Integer) test1);
        Object test2 = stringObjectValueOperations.get("test2");
        Assertions.assertTrue(2.0 == (Double) test2);

    }




}
