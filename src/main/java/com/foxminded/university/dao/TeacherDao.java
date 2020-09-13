package com.foxminded.university.dao;

import com.foxminded.university.domain.Teacher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TeacherDao extends Dao<Teacher> {

    Teacher getById(int id);

    List<Teacher> getAll();

    void save(Teacher teacher);

    void update(Teacher teacher);

    void delete(int id);
}
