package com.spring.mybatis.interceptor;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.spring.mybatis.annotation.SensitiveData;
import com.spring.mybatis.utils.DecryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName DecryptInterceptor
 * @Create 2023/4/11 17:59
 * @Description 实现出参解密拦截器
 */
@Slf4j
@Component
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
public class DecryptInterceptor implements Interceptor {

    @Value("${secretKey}")
    private String secretKey;
 
    @Autowired
    private DecryptUtil aesDecrypt;
 
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //取出查询的结果
        Object resultObject = invocation.proceed();
        if (Objects.isNull(resultObject)) {
            return null;
        }
        //基于selectList
        if (resultObject instanceof ArrayList) {
            ArrayList resultList = (ArrayList) resultObject;
            if (!CollectionUtils.isEmpty(resultList) && needToDecrypt(resultList.get(0))) {
                for (Object result : resultList) {
                    //逐一解密
                    aesDecrypt.decrypt(secretKey, result);
                }
            }
        //基于selectOne
        } else {
            if (needToDecrypt(resultObject)) {
                aesDecrypt.decrypt(secretKey, resultObject);
            }
        }
        return resultObject;
    }
 
    private boolean needToDecrypt(Object object) {
        Class<?> objectClass = object.getClass();
        SensitiveData sensitiveData = AnnotationUtils.findAnnotation(objectClass, SensitiveData.class);
        return Objects.nonNull(sensitiveData);
    }
 
 
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
 
    @Override
    public void setProperties(Properties properties) {
 
    }
}