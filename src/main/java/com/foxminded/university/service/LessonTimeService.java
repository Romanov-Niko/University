package com.foxminded.university.service;

import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.domain.LessonTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LessonTimeService {

    @Value("${maxLessonDuration}")
    private int maxLessonDuration;

    private final LessonTimeDao lessonTimeDao;

    public LessonTimeService(LessonTimeDao lessonTimeDao) {
        this.lessonTimeDao = lessonTimeDao;
    }

    public List<LessonTime> getAll() {
        return lessonTimeDao.getAll();
    }

    public void save(LessonTime lessonTime) {
        if (isDurationCorrect(lessonTime) && isLessonTimeUnique(lessonTime)) {
            lessonTimeDao.save(lessonTime);
        }
    }

    public void update(LessonTime lessonTime) {
        if (isLessonTimePresent(lessonTime.getId()) && isDurationCorrect(lessonTime)) {
            lessonTimeDao.update(lessonTime);
        }
    }

    public void delete(int id) {
        lessonTimeDao.delete(id);
    }

    private boolean isLessonTimePresent(int id) {
        return lessonTimeDao.getById(id).isPresent();
    }

    private boolean isDurationCorrect (LessonTime lessonTime) {
        return Duration.between(lessonTime.getBegin(), lessonTime.getEnd()).toMinutes() < maxLessonDuration
                && Duration.between(lessonTime.getBegin(), lessonTime.getEnd()).toMinutes() > 0;
    }

    private boolean isLessonTimeUnique(LessonTime lessonTime) {
        return !lessonTimeDao.getByStartAndEndTime(lessonTime.getBegin(), lessonTime.getEnd()).isPresent();
    }
}
