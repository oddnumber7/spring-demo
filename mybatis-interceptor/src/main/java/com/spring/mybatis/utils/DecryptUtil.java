package com.spring.mybatis.utils;

/**
 * @author 16247
 * 定义解密接口
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