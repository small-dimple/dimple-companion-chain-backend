package com.smlDimple.dimpleCompanionChain.service;

import com.smlDimple.dimpleCompanionChain.mapper.UserMapper;
import com.smlDimple.dimpleCompanionChain.model.domain.User;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author: small-dimple
 **/
@SpringBootTest

public class InsertUsersTest {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    /**
     * 线程池 40个线程 最大1000个线程 new 的是10000个任务；第一个10000是过期时间单位是毫秒
     */
    private ExecutorService executorService = new ThreadPoolExecutor(40, 1000, 10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));


    /**
     * 单线程批量插入用户
     */
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

    /**
     * 并发批量插入用户
     */
    @Test
    public void doConcurrencyInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 分十组
        int batchSize = 5000;
        int j = 0;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            List<User> userList = new ArrayList<>();
            while (true) {
                j++;
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

                userList.add(user);
                if (j % batchSize == 0) {
                    break;
                }
            }
            // 异步执行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("threadName: " + Thread.currentThread().getName());
                userService.saveBatch(userList, batchSize);
            }, executorService);
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
        // 20 秒 10 万条
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}



