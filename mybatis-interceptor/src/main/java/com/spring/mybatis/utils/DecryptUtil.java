package com.spring.mybatis.utils;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName DecryptUtil
 * @Create 2023/4/11 17:59
 * @Description 定义解密接口
 */
public interface DecryptUtil {
 
    /**
     * 解密
     *
     * @param result resultType的实例
     * @return T
     * @throws IllegalAccessException 字段不可访问异常
     */
     <T> T decrypt(String secretKey, T result) throws IllegalAccessException;
    
}