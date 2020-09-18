package com.foxminded.university.service;

import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.domain.LessonTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LessonTimeService implements Service<LessonTime>{

    private final LessonTimeDao lessonTimeDao;

    @Autowired
    public LessonTimeService(LessonTimeDao lessonTimeDao) {
        this.lessonTimeDao = lessonTimeDao;
    }

    @Override
    public LessonTime getById(int id) {
        return lessonTimeDao.getById(id);
    }

    @Override
    public List<LessonTime> getAll() {
        return lessonTimeDao.getAll();
    }

    @Override
    public void save(LessonTime lessonTime) {
        lessonTimeDao.save(lessonTime);
    }

    @Override
    public void update(LessonTime lessonTime) {
        lessonTimeDao.update(lessonTime);
    }

    @Override
    public void delete(int id) {
        lessonTimeDao.delete(id);
    }
}
