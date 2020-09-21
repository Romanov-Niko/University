package com.foxminded.university.dao;

import com.foxminded.university.domain.Lesson;
import org.springframework.stereotype.Component;

import java.util.List;

public interface LessonDao extends Dao<Lesson> {

    List<Lesson> getAllByDayId(int id);
}
