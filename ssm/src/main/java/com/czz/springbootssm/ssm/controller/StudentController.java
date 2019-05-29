package com.czz.springbootssm.ssm.controller;

import com.czz.springbootssm.ssm.pojo.Student;
import com.czz.springbootssm.ssm.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class StudentController {
    @Autowired
    private StudentService studentService;

    @RequestMapping("QueryStudent")
    @ResponseBody
    public List<Student> QueryStudent(){
        return studentService.QueryStudent();
    }
}
