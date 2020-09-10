package com.foxminded.university.dao;

import com.foxminded.university.domain.Lesson;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

public interface LessonDao extends Dao<Lesson> {

    @Override
    Lesson getById(int id);

    @Override
    List<Lesson> getAll();

    @Override
    void save(Lesson lesson);

    @Override
    void update(Lesson lesson);

    @Override
    void delete(int id);

    List<Lesson> getAllByDayId(int id);
}
