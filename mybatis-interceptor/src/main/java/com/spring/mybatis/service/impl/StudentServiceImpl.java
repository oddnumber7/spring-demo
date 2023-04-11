package com.spring.mybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.mybatis.entity.Student;
import com.spring.mybatis.mapper.StudentMapper;
import com.spring.mybatis.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 16247
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public boolean updateByXml(int id, int age, String name) {
        return studentMapper.updateByXml(id, age, name);
    }

    @Override
    public boolean insertDao(int age, String name, String addr) {
        return studentMapper.insertDao(age, name, addr);
    }

}
