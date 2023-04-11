package com.spring.mybatis.utils;

import java.lang.reflect.Field;

/**
 * @author 16247
 * 定义加密接口
 */
public interface EncryptUtil {

    /**
     * 加密
     * @param secretKey 密钥
     * @param declaredFields paramsObject所声明的字段
     * @param paramsObject mapper中paramsType的实例
     * @return T
     * @throws IllegalAccessException 字段不可访问异常
     */
    <T> T encrypt(String secretKey, Field[] declaredFields, T paramsObject) throws IllegalAccessException;

    String encrypt(String secretKey, Object data);

}