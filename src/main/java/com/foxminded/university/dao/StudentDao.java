package com.foxminded.university.dao;

import com.foxminded.university.domain.Student;

import java.util.List;

public interface StudentDao extends Dao<Student> {

    Student getById(int id);

    List<Student> getAll();

    void save(Student student);

    void update(Student student);

    void delete(int id);

    List<Student> getAllByGroupId(int id);
}
