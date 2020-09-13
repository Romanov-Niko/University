package com.foxminded.university.dao;

import com.foxminded.university.domain.Lesson;

import java.util.List;

public interface LessonDao extends Dao<Lesson> {

    Lesson getById(int id);

    List<Lesson> getAll();

    void save(Lesson lesson);

    void update(Lesson lesson);

    void delete(int id);

    List<Lesson> getAllByDayId(int id);
}
