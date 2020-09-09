package com.foxminded.university.dao;

import com.foxminded.university.domain.LessonTime;
import org.springframework.stereotype.Component;

import java.util.List;

public interface LessonTimeDao extends Dao<LessonTime> {

    @Override
    LessonTime getById(int id);

    @Override
    List<LessonTime> getAll();

    @Override
    void save(LessonTime lessonTime);

    @Override
    void update(LessonTime lessonTime);

    @Override
    void delete(int id);
}
