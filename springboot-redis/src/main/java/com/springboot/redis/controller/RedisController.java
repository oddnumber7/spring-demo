package com.springboot.redis.controller;

import com.springboot.redis.utils.JedisUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName RedisController
 * @Create 2023/4/12 14:03
 * @Description
 */
@RestController
@RequestMapping(value = "/api/redis")
public class RedisController {

    @Autowired
    private JedisUtils jedisUtils;

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping(value = "/set")
    public void set(){
        jedisUtils.set("fly", "fish");
        jedisUtils.set("fly1", "fish1");
        jedisUtils.set("fly", "fish", 1);
        jedisUtils.set("fly1", "fish1", 1);
    }

    @GetMapping(value = "/get")
    public void get(){
        String get = jedisUtils.get("fly");
        System.out.println("get = " + get);
        String get1 = jedisUtils.get("fly",  1);
        System.out.println("get1 = " + get1);
    }

    @GetMapping(value = "/delete")
    public void delete(){
        boolean delete = jedisUtils.delete(new String[]{"fly", "fly1"});
        System.out.println("delete = " + delete);
        boolean delete1 = jedisUtils.delete(new String[]{"fly", "fly1"}, 1);
        System.out.println("delete1 = " + delete1);
    }

    @GetMapping(value = "/hashSet")
    public void hashSet(){
        jedisUtils.hashSet("fly", "fish", "嘻嘻");
        jedisUtils.hashSet("fly", "fish1", "嘻嘻");
        jedisUtils.hashSet("fly", "fish", "嘻嘻", 1);
        jedisUtils.hashSet("fly", "fish1", "嘻嘻", 1);
        Map<String, String> map = jedisUtils.hashGetAll("fly");
        System.out.println("map = " + map);
        Map<String, String> map1 = jedisUtils.hashGetAll("fly", 1);
        System.out.println("map1 = " + map1);
    }

    @GetMapping(value = "/lock")
    public void lock() throws InterruptedException {
        //获取锁(可重入)，指定锁的名称
        RLock lock = redissonClient.getLock("FlyFish");
        //尝试获取锁，参数分别是：获取锁的最大等待时间(期间会重试)，锁自动释放时间，时间单位
        try {
            if (lock.tryLock(10, 20, TimeUnit.SECONDS)){
                int stock = Integer.parseInt(jedisUtils.get("stock"));
                if (stock > 0){
                    int realStock = stock - 1;
                    jedisUtils.set("stock", realStock + "");
                    System.out.println("扣减库存成功，剩余：" + realStock);
                }else {
                    System.out.println("扣减失败，库存不足！");
                }
            }
        }finally {
            lock.unlock();
        }

    }

}
