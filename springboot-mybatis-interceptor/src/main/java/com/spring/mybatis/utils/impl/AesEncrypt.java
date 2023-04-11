package com.spring.mybatis.utils.impl;


import com.spring.mybatis.annotation.SensitiveField;
import com.spring.mybatis.utils.AesUtil;
import com.spring.mybatis.utils.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName AesEncrypt
 * @Create 2023/4/11 17:59
 * @Description AES工具加密实现类
 */
@Component
public class AesEncrypt implements EncryptUtil {

    @Autowired
    private AesUtil aesUtil;

    /**
     * 加密
     *
     * @param declaredFields paramsObject所声明的字段
     * @param paramsObject   mapper中paramsType的实例
     * @return T
     */
    @Override
    public <T> T encrypt(String secretKey, Field[] declaredFields, T paramsObject) throws IllegalAccessException {
        for (Field field : declaredFields) {
            //取出所有被EncryptDecryptField注解的字段
            SensitiveField sensitiveField = field.getAnnotation(SensitiveField.class);
            if (!Objects.isNull(sensitiveField)) {
                //反反射对象在使用时应禁止Java语言访问检查
                field.setAccessible(true);
                Object object = field.get(paramsObject);
                //暂时只实现String类型的加密
                if (object instanceof String) {
                    String value = (String) object;
                    //加密
                    try {
                        field.set(paramsObject, aesUtil.encrypt(secretKey, value));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return paramsObject;
    }

    /**
     * 单字段加密
     *
     * @param secretKey 密钥
     * @param data      要加密的参数
     * @return 密文
     */
    @Override
    public String encrypt(String secretKey, Object data) {
        String encryptData = null;
        try {
            encryptData = aesUtil.encrypt(secretKey, (String) data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptData;
    }

}
