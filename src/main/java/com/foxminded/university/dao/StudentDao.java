package com.foxminded.university.dao;

import com.foxminded.university.domain.Student;

import java.util.List;

public interface StudentDao extends Dao<Student> {

    List<Student> getAllByGroupId(int id);

    List<Student> getAllByGroupName(String name);
}
