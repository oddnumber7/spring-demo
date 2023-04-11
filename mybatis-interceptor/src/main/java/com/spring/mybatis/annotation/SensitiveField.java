package com.spring.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @author 16247
 * 注解敏感信息类中敏感字段的注解
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveField {

}
