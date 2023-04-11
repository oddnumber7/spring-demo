package com.spring.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @author 16247
 * 注解敏感信息类的注解
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveData {

}