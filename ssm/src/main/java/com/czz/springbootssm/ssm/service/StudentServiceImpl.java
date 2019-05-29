package com.czz.springbootssm.ssm.service;

import com.czz.springbootssm.ssm.dao.StudentDao;
import com.czz.springbootssm.ssm.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDao studentDao;

    @Override
    public List<Student> QueryStudent() {
        return studentDao.QueryStudent();
    }
}
