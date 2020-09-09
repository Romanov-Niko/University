package com.foxminded.university.dao;

import com.foxminded.university.domain.Student;
import org.springframework.stereotype.Component;

import java.util.List;

public interface StudentDao extends Dao<Student> {

    @Override
    Student getById(int id);

    @Override
    List<Student> getAll();

    @Override
    void save(Student student);

    @Override
    void update(Student student);

    @Override
    void delete(int id);

    List<Student> getAllByGroup(int id);
}
