package com.foxminded.university.dao;

import com.foxminded.university.domain.Teacher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TeacherDao extends Dao<Teacher> {

    @Override
    Teacher getById(int id);

    @Override
    List<Teacher> getAll();

    @Override
    void save(Teacher teacher);

    @Override
    void update(Teacher teacher);

    @Override
    void delete(int id);
}
