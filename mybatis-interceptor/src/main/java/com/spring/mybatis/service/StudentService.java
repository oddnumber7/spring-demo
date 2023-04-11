package com.spring.mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.mybatis.entity.Student;

/**
 * @author 16247
 */
public interface StudentService extends IService<Student> {

    boolean updateByXml(int id, int age, String name);

    boolean insertDao(int age, String name, String addr);

}
