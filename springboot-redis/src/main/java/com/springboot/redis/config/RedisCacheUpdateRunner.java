package com.springboot.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName RedisCacheUpdateRunner
 * @Create 2023/4/12 14:00
 * @Description Redis启动删除缓存
 */
@Component
@Slf4j
@Order(10)
public class RedisCacheUpdateRunner implements CommandLineRunner {

    @Autowired
    private JedisPool jedisPool;

    @Override
    public void run(String... args) throws Exception {
        Jedis jedis = jedisPool.getResource();
        jedis.flushAll();
        log.info("------------->>>>>>redis server 启动清空缓存结束");
    }
}
