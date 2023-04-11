package com.spring.mybatis.interceptor;

import com.spring.mybatis.annotation.SensitiveData;
import com.spring.mybatis.utils.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.*;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName EncryptInterceptor
 * @Create 2023/4/11 17:59
 * @Description
 * 数据库更新操作拦截器
 * 支持的使用场景
 * ①场景一：通过mybatis-plus BaseMapper自动映射的方法
 * ②场景一：通过mapper接口自定义的方法
 */
@Slf4j
@Component
@Intercepts({
        // type 指定代理对象，method 指定代理方法，args 指定type代理类中method方法的参数
        @Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class})
})
public class EncryptInterceptor implements Interceptor {

    @Value("${secretKey}")
    private String secretKey;

    /**
     * 修改时ParamMap中包含参数的前缀
     */
    private static final String UPDATE_PRE = "et";

    private final EncryptUtil encryptUtil;

    @Autowired
    public EncryptInterceptor(EncryptUtil encryptUtil) {
        this.encryptUtil = encryptUtil;
    }

    /**
     * 维护要加密的表和字段
     */
    private final static Map<String, List<String>> TABLE_MAP = new HashMap<String, List<String>>() {{
        put("t_student",
                new ArrayList<String>() {{
                    add("name");
                }}
        );
    }};

    /**
     * 1.判断插入的是对象还是多个参数，如果是多个参数会包装在ParamMap中
     * 2.如果是对象判断类是否被@Sensitive所注解，如果是ParamMap判断Map中是否有维护的字段
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //@Signature指定了type=ParameterHandler后，这里的invocation.getTarget()就是ParameterHandler
        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
        //获取参数对像，即mapper中paramsType的实例
        Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
        parameterField.setAccessible(true);
        Object parameterObject = parameterField.get(parameterHandler);
        if (Objects.isNull(parameterObject)) {
            //无参数，直接放行
            return invocation.proceed();
        }
        //获取参数类型
        Class<?> parameterObjectClass = parameterObject.getClass();
        //MappedStatement,存储了SQL对应的所有信息
        Field mappedStatementField = parameterHandler.getClass().getDeclaredField("mappedStatement");
        mappedStatementField.setAccessible(true);
        MappedStatement mappedStatement = (MappedStatement) mappedStatementField.get(parameterHandler);
        //获取BoundSql对象，此对象包含生成的SQL和参数的Map映射
        Field boundSqlField = parameterHandler.getClass().getDeclaredField("boundSql");
        boundSqlField.setAccessible(true);
        BoundSql boundSql = (BoundSql) boundSqlField.get(parameterHandler);
        //获取要插入的参数类型，如果是@Param参数形式则为NULL
        Class<?> clazz = mappedStatement.getParameterMap().getType();
        //对象操作
        if (parameterObjectClass == clazz) {
            //判断实体是否被@SensitiveData所注解
            if (needToEncrypt(clazz)) {
                //获取实体类的所有字段
                Field[] fields = parameterObjectClass.getDeclaredFields();
                encryptUtil.encrypt(secretKey, fields, parameterObject);
            }
            return invocation.proceed();
        }
        //修改操作会对paramMap重命名
        if (parameterObject instanceof MapperMethod.ParamMap && ((MapperMethod.ParamMap<?>) parameterObject).containsKey(UPDATE_PRE)) {
            Map<String, Object> paramMap = (HashMap<String, Object>) parameterObject;
            Object updateParam = paramMap.get(UPDATE_PRE);
            Class<?> updateClass = updateParam.getClass();
            if (needToEncrypt(updateClass)){
                Field[] fields = updateClass.getDeclaredFields();
                encryptUtil.encrypt(secretKey, fields, updateParam);
            }
            return invocation.proceed();
        }
        //XML操作
        if (parameterObject instanceof MapperMethod.ParamMap) {
            Map<String, Object> paramMap = (HashMap<String, Object>) parameterObject;
            //判断表是否存在维护的解密Map中
            List<String> encryptList = getEncryptList(paramMap, boundSql.getSql());
            if (encryptList != null && encryptList.size() > 0){
                //存在需要加密的字段，进行加密
                for (String encryptParam : encryptList) {
                    if (paramMap.get(encryptParam) != null){
                        Object data = paramMap.get(encryptParam);
                        String encryptData = encryptUtil.encrypt(secretKey, data);
                        paramMap.put(encryptParam, encryptData);
                    }
                }
            }
            invocation.proceed();
        }
        //其他情况直接放行
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 判断类是否被@SensitiveData所注解
     *
     * @param clazz 实体对象
     * @return 是否被@SensitiveData所注解
     */
    public boolean needToEncrypt(Class<?> clazz) {
        SensitiveData sensitiveData = AnnotationUtils.findAnnotation(clazz, SensitiveData.class);
        return sensitiveData != null;
    }

    /**
     * 获取加密的字段
     *
     * @param paramMap 参数列表
     * @param sql      执行的SQL
     * @return 所有需要加密的字段
     */
    public List<String> getEncryptList(Map<String, Object> paramMap, String sql) {
        Set<String> tableName = TABLE_MAP.keySet();
        for (String table : tableName) {
            //判断是否包含该表
            if (sql.contains(table)) {
                //判断加密字段是否存在
                List<String> paramName = TABLE_MAP.get(table);
                String exist = paramMap.keySet().stream().filter(paramName::contains).findAny().orElse(null);
                if (exist != null) {
                    return paramName;
                }
            }
        }
        return new ArrayList<>();
    }

}

