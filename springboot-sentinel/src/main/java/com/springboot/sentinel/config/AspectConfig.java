package com.springboot.sentinel.config;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName SentinelApplication
 * @Create 2023/4/12 21:55
 * @Description
 */
@Configuration
public class AspectConfig {

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

}
