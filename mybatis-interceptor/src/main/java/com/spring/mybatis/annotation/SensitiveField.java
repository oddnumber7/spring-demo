package com.spring.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName SensitiveField
 * @Create 2023/4/11 17:59
 * @Description 注解敏感信息类中敏感字段的注解
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveField {

}
