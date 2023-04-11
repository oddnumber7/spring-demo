package com.springboot.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.mybatis.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName StudentMapper
 * @Create 2023/4/11 17:59
 * @Description
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
