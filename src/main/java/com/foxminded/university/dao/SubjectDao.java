package com.foxminded.university.dao;

import com.foxminded.university.domain.Subject;
import org.springframework.stereotype.Component;

import java.util.List;

public interface SubjectDao extends Dao<Subject> {

    @Override
    Subject getById(int id);

    @Override
    List<Subject> getAll();

    @Override
    void save(Subject subject);

    @Override
    void update(Subject subject);

    @Override
    void delete(int id);
}
