package com.smlDimple.dimpleCompanionChain.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smlDimple.dimpleCompanionChain.model.domain.User;
import com.smlDimple.dimpleCompanionChain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 定时刷新缓存
 *
 * @Author: small-dimple
 **/
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserService userService;

    @Resource
    private RedissonClient redissonClient;

    //重点用户
    private List<Long> mainUserList = List.of(1L);

    @Scheduled(cron = "0 0 0 * * ?") //每天凌晨执行
    public void doCacheRecommendUser(){
        RLock lock = redissonClient.getLock("dimple:precachejob:docache:lock");
        try {
            if(lock.tryLock(0,-1, TimeUnit.MILLISECONDS))
            {
                for(long userId: mainUserList){
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);

                    String redisKey = String.format("dimple:user:recommend:%s",userId);
                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();


                    try {
                /*
                ！！！！！！！！！！！！！一定要设置过期时间
                 */
                        valueOperations.set(redisKey,userPage,30000, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        log.error("redis set key error",e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("doCacheRecommendUser error",e);
        }finally {
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }


    }
}
