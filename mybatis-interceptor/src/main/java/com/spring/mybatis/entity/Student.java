package com.spring.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.spring.mybatis.annotation.SensitiveData;
import com.spring.mybatis.annotation.SensitiveField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 16247
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SensitiveData
@TableName(value = "t_student")
public class Student implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "name")
    @SensitiveField
    private String name;

    @TableField("age")
    private Integer age;

    @TableField("addr")
    private String addr;

    public Student(String name, Integer age, String addr) {
        this.name = name;
        this.age = age;
        this.addr = addr;
    }

}
