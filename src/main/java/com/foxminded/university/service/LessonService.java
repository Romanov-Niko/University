package com.foxminded.university.service;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.domain.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LessonService implements Service<Lesson>{

    private final LessonDao lessonDao;

    @Autowired
    public LessonService(LessonDao lessonDao) {
        this.lessonDao = lessonDao;
    }

    @Override
    public Lesson getById(int id) {
        return lessonDao.getById(id);
    }

    @Override
    public List<Lesson> getAll() {
        return lessonDao.getAll();
    }

    @Override
    public void save(Lesson lesson) {
        lessonDao.save(lesson);
    }

    @Override
    public void update(Lesson lesson) {
        lessonDao.update(lesson);
    }

    @Override
    public void delete(int id) {
        lessonDao.delete(id);
    }

    public List<Lesson> getAllByDayId(int id) {
        return lessonDao.getAllByDayId(id);
    }
}
