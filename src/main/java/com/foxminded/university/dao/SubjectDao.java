package com.foxminded.university.dao;

import com.foxminded.university.domain.Subject;

import java.util.List;

public interface SubjectDao extends Dao<Subject> {

    Subject getById(int id);

    List<Subject> getAll();

    void save(Subject subject);

    void update(Subject subject);

    void delete(int id);

    List<Subject> getAllByTeacherId(int id);
}
