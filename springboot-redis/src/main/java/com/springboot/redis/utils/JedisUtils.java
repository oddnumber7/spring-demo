package com.springboot.redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName JedisUtils
 * @Create 2023/4/12 14:00
 * @Description
 */
@Slf4j
@Component
public class JedisUtils {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 获取Jedis资源
     */
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 释放Jedis连接
     */
    public void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * 通过key获取储存在redis中的value并释放连接
     *
     * @param key 键
     * @return 成功返回value 失败返回null
     */
    public String get(String key) {
        Jedis jedis = getJedis();
        String value = null;
        try {
            value = jedis.get(key);
        } catch (Exception e) {
            log.error("jedis get error [{}]", e.getMessage());
        } finally {
            close(jedis);
        }
        return value;
    }

    /**
     * 通过key获取储存在redis中的value并释放连接
     *
     * @param key     key
     * @param indexDb 选择redis库 0-15
     * @return 成功返回value 失败返回null
     */
    public String get(String key, int indexDb) {
        String value = null;
        Jedis jedis = getJedis();
        try {
            jedis.select(indexDb);
            value = jedis.get(key);
        } catch (Exception e) {
            log.error("jedis get error [{}]", e.getMessage());
        } finally {
            close(jedis);
        }
        return value;
    }

    /**
     * 普通的set值
     *
     * @param key 键
     * @param val 值
     */
    public String set(String key, String val) {
        Jedis jedis = getJedis();
        String result = null;
        try {
            result = jedis.set(key, val);
        } catch (Exception e) {
            log.error("jedis set error [{}]", e.getMessage());
        } finally {
            close(jedis);
        }
        return result;
    }

    /**
     * 普通的set值
     *
     * @param key     键
     * @param val     值
     * @param indexDb 数据库索引
     */
    public String set(String key, String val, int indexDb) {
        Jedis jedis = getJedis();
        String result = null;
        try {
            jedis.select(indexDb);
            result = jedis.set(key, val);
        } catch (Exception e) {
            log.error("jedis set error [{}]", e.getMessage());
        } finally {
            close(jedis);
        }
        return result;
    }

    /**
     * 根据key删除 可以传入多个key
     *
     * @param key 键
     * @return
     */
    public boolean delete(String[] key) {
        Jedis jedis = getJedis();
        try {
            jedis.del(key);
            log.info("redis del key=[{}]", Arrays.asList(key));
            return true;
        } catch (Exception e) {
            log.error("jedis del error [{}]", e.getMessage());
            return false;
        } finally {
            close(jedis);
        }
    }


    /**
     * 根据key删除 可以穿入多个key
     *
     * @param key     键
     * @param indexDb 数据库索引
     * @return
     */
    public boolean delete(String[] key, int indexDb) {
        Jedis jedis = getJedis();
        try {
            jedis.select(indexDb);
            jedis.del(key);
            log.info("redis del key=[{}]", Arrays.asList(key));
            return true;
        } catch (Exception e) {
            log.error("jedis del error [{}]", e.getMessage());
            return false;
        } finally {
            close(jedis);
        }
    }

    /**
     * hash set 设置值
     *
     * @param key     键
     * @param hashKey 哈希键
     * @param value   值
     * @return
     */
    public Long hashSet(String key, String hashKey, String value) {
        Jedis jedis = getJedis();
        try {
            return jedis.hset(key, hashKey, value);
        } catch (Exception e) {
            log.error("jedis hashSet error [{}]", e.getMessage());
            return 0L;
        } finally {
            close(jedis);
        }
    }

    /**
     * hash set 设置值
     *
     * @param key     键
     * @param hashKey 哈希键
     * @param value   值
     * @param indexDb 数据库索引
     * @return
     */
    public Long hashSet(String key, String hashKey, String value, int indexDb) {
        Jedis jedis = getJedis();
        try {
            jedis.select(indexDb);
            return jedis.hset(key, hashKey, value);
        } catch (Exception e) {
            log.error("jedis hashSet error [{}]", e.getMessage());
            return 0L;
        } finally {
            close(jedis);
        }
    }

    /**
     * 根据key获取所有value
     *
     * @param key     哈希键
     * @return
     */
    public Map<String, String> hashGetAll(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.hgetAll(key);
        } catch (Exception e) {
            log.error("jedis hget error [{}]", e.getMessage());
            return new HashMap<>(16);
        } finally {
            close(jedis);
        }
    }

    /**
     * 根据key获取所有value
     *
     * @param key     哈希键
     * @param indexDb 数据库索引
     * @return
     */
    public Map<String, String> hashGetAll(String key, int indexDb) {
        Jedis jedis = getJedis();
        try {
            jedis.select(indexDb);
            return jedis.hgetAll(key);
        } catch (Exception e) {
            log.error("jedis hget error [{}]", e.getMessage());
            return new HashMap<>(16);
        } finally {
            close(jedis);
        }
    }

    /**
     * 根据key和hashkey获取
     *
     * @param key
     * @param hashKey
     * @param indexDb
     * @return
     */
    public String hget(String key, String hashKey, int indexDb) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(indexDb);
            String hget = jedis.hget(key, hashKey);
            return hget;
        } catch (Exception e) {
            log.error("jedis hget error [{}]", e.getMessage());
            return "";
        } finally {
            close(jedis);
        }
    }

    /**
     * 判断hashkey是否存在
     *
     * @param key
     * @param hashKey
     * @param indexDb
     * @return
     */
    public Boolean hexists(String key, String hashKey, int indexDb) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(indexDb);
            Boolean hexists = jedis.hexists(key, hashKey);
            return hexists;
        } catch (Exception e) {
            log.error("jedis hexists error [{}]", e.getMessage());
            return false;
        } finally {
            close(jedis);
        }
    }

    public Long hdel(String key, String hashKey, int indexDb) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(indexDb);
            return jedis.hdel(key, hashKey);
        } catch (Exception e) {
            log.error("jedis hexists error [{}]", e.getMessage());
            return Long.valueOf("-1");
        }
    }

    /**
     * 根据排序获取分页数据，修改min，max可以在某一个区间选择
     *
     * @param key
     * @param offset
     * @param count
     * @param indexDb
     * @return
     */
    public Set<String> zrangeByScore(String key, final int offset, final int count, int indexDb) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(indexDb);
            // min 和 max 可以是 -inf 和 +inf ，这样一来，你就可以在不知道有序集的最低和最高 score 值的情况下，使用 ZRANGEBYSCORE 这类命令。
            Set<String> set = jedis.zrangeByScore(key, "-inf", "+inf", offset, count);
            return set;
        } catch (Exception e) {
            log.error("jedis zrangeByScore error [{}]", e.getMessage());
            return null;
        } finally {
            close(jedis);
        }
    }

}

