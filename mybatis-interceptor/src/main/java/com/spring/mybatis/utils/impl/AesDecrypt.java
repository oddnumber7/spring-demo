package com.spring.mybatis.utils.impl;


import com.spring.mybatis.annotation.SensitiveField;
import com.spring.mybatis.utils.AesUtil;
import com.spring.mybatis.utils.DecryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName AesDecrypt
 * @Create 2023/4/11 17:59
 * @Description AES工具解密实现类
 */
@Component
public class AesDecrypt implements DecryptUtil {
    
    @Autowired
    private AesUtil aesUtil;
    
    /**
     * 解密
     *
     * @param result resultType的实例
     * @return T
     * @throws IllegalAccessException 字段不可访问异常
     */
    @Override
    public <T> T decrypt(String secretKey, T result) throws IllegalAccessException {
        //取出resultType的类
        Class<?> resultClass = result.getClass();
        Field[] declaredFields = resultClass.getDeclaredFields();
        for (Field field : declaredFields) {
            //取出所有被EncryptDecryptField注解的字段
            SensitiveField sensitiveField = field.getAnnotation(SensitiveField.class);
            if (!Objects.isNull(sensitiveField)) {
                field.setAccessible(true);
                Object object = field.get(result);
                //只支持String的解密
                if (object instanceof String) {
                    String value = (String) object;
                    //对注解的字段进行逐一解密
                    try {
                        field.set(result, aesUtil.decrypt(secretKey, value));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }
}