package com.springboot.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName RedisConfig
 * @Create 2023/4/12 14:00
 * @Description
 */
@Slf4j
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxWaitMillis;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minWaitMillis;

    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxWaitMillis);
        jedisPoolConfig.setMinIdle(minWaitMillis);
        jedisPoolConfig.setMaxTotal(maxActive);
        // 是否启用pool的jmx管理功能, 默认true
        jedisPoolConfig.setJmxEnabled(true);
        log.info("JedisPool  start ---------------->>>>>>>>>>> host [{}],port [{}]",host,port);
        return new JedisPool(jedisPoolConfig, host, port, timeout, password);
    }

}
