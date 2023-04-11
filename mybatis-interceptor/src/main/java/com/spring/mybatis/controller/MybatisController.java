package com.spring.mybatis.controller;

import com.spring.mybatis.entity.Student;
import com.spring.mybatis.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName MybatisController
 * @Create 2023/4/11 17:59
 * @Description
 */
@RestController
@RequestMapping(value = "/api/mybatis")
public class MybatisController {

    @Autowired
    private StudentService studentService;

    @GetMapping(value = "/get")
    public List<Student> encrypt() {
        return studentService.list();
    }

    @GetMapping(value = "/insert")
    public void insert() {
        Student student = new Student("FlyFish", 18, "深圳");
        studentService.save(student);
    }

    @GetMapping(value = "/insertByXml")
    public void insertByXml() {
        studentService.insertDao( 17, "FlyFish","深圳");
    }

    @GetMapping(value = "/insertBatch")
    public void insertBatch() {
        List<Student> studentList = new ArrayList<Student>(){{
            add(new Student("FlyFish", 18, "深圳"));
            add(new Student("FlyFish", 19, "深圳"));
        }};
        studentService.saveBatch( studentList);
    }

    @GetMapping(value = "/delete")
    public void delete(){
        studentService.removeById(1);
    }

    @PutMapping(value = "/update")
    public void update(@RequestBody Student student) {
        studentService.updateById(student);
    }

    @GetMapping(value = "/updateByXml")
    public void updateByXml() {
        studentService.updateByXml(4,6, "FlyFish");
    }

    @GetMapping(value = "/page/{page}/{size}")
    public List<Student> page(@PathVariable("page") Integer page,
                                 @PathVariable("size") Integer size){
        return studentService.page(page, size);
    }

}
