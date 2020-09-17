package com.foxminded.university.dao;

import com.foxminded.university.domain.Lesson;

import java.util.List;

public interface LessonDao extends Dao<Lesson> {

    List<Lesson> getAllByDayId(int id);
}
