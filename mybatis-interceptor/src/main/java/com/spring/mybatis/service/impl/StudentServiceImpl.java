package com.spring.mybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.spring.mybatis.entity.Student;
import com.spring.mybatis.mapper.StudentMapper;
import com.spring.mybatis.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName StudentServiceImpl
 * @Create 2023/4/11 17:59
 * @Description
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

    @Override
    public List<Student> page(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Student> studentList = studentMapper.selectList(null);
        long total = ((Page) studentList).getTotal();
        System.out.println("total = " + total);
        return studentList;
    }
}
