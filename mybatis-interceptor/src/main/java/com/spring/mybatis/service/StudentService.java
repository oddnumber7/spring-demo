package com.spring.mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.mybatis.entity.Student;

import java.util.List;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName StudentService
 * @Create 2023/4/11 17:59
 * @Description
 */
public interface StudentService extends IService<Student> {

    boolean updateByXml(int id, int age, String name);

    boolean insertDao(int age, String name, String addr);

    List<Student> page(Integer page, Integer size);

}
