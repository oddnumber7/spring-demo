package com.springboot.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName SensitiveData
 * @Create 2023/4/11 17:59
 * @Description 注解敏感信息类的注解
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveData {

}