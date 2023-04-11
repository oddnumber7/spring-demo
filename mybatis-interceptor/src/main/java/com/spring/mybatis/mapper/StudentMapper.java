package com.spring.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.mybatis.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 16247
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    boolean updateByXml(@Param(value = "id") int id,
                     @Param(value = "age") int age,
                     @Param(value = "name") String name);

    boolean insertDao(@Param(value = "age") int age,
                      @Param(value = "name") String name,
                      @Param(value = "addr") String addr);

}
