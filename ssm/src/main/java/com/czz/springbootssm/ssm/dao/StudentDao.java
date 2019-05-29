package com.czz.springbootssm.ssm.dao;

import com.czz.springbootssm.ssm.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StudentDao {
    public List<Student> QueryStudent();
}
