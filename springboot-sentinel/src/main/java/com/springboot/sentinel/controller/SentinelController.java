package com.springboot.sentinel.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName SentinelController
 * @Create 2023/4/12 21:59
 * @Description
 */
@RestController
@RequestMapping(value = "/api/sentinel")
public class SentinelController {

    /**
     * 定义资源的方式
     * @return
     */
    @GetMapping(value = "/hello")
    public String hello(){
        try(Entry hello = SphU.entry("hello");) {
            return "hello";
        } catch (BlockException e) {
            return "系统繁忙,请稍后";
        }
    }

    /**
     * 注解方式
     * @return
     */
    @SentinelResource(value = "Sentinel_Annotation", blockHandler = "exceptionHandler", fallback = "fallback")
    @GetMapping("/annotation")
    public String annotation() {
        return "Hello Sentinel";
    }

    /**
     * 熔断方法
     * @return 内容
     */
    public String fallback(){
        return "访问频率过快";
    }

}
