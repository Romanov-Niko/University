package com.foxminded.university.dao;

import com.foxminded.university.domain.LessonTime;

import java.util.List;

public interface LessonTimeDao extends Dao<LessonTime> {

    LessonTime getById(int id);

    List<LessonTime> getAll();

    void save(LessonTime lessonTime);

    void update(LessonTime lessonTime);

    void delete(int id);
}
