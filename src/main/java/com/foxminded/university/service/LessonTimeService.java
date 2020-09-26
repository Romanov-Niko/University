package com.foxminded.university.service;

import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.domain.LessonTime;
import com.foxminded.university.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class LessonTimeService {

    private static final Logger logger = LoggerFactory.getLogger(LessonTimeService.class);

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
        logger.debug("Check consistency of lesson time with id {} before saving", lessonTime.getId());
        if (isDurationConsistent(lessonTime) && isLessonTimeUnique(lessonTime)) {
            lessonTimeDao.save(lessonTime);
        }
    }

    public void update(LessonTime lessonTime) {
        logger.debug("Check consistency of lesson time with id {} before updating", lessonTime.getId());
        if (isLessonTimePresent(lessonTime.getId()) && isDurationConsistent(lessonTime)) {
            lessonTimeDao.update(lessonTime);
        }
    }

    public void delete(int id) {
        lessonTimeDao.delete(id);
    }

    private boolean isLessonTimePresent(int id) {
        return lessonTimeDao.getById(id).map(obj -> true).orElseThrow(() -> new EntityNotFoundException(String.format("Lesson time with id %d is not present", id)));
    }

    private boolean isDurationConsistent(LessonTime lessonTime) {
        if (Duration.between(lessonTime.getBegin(), lessonTime.getEnd()).toMinutes() < maxLessonDuration
                && Duration.between(lessonTime.getBegin(), lessonTime.getEnd()).toMinutes() > 0) {
            return true;
        } else {
            throw new LessonDurationOutOfBoundsException("Lesson duration is out of bounds");
        }
    }

    private boolean isLessonTimeUnique(LessonTime lessonTime) {
        lessonTimeDao.getByStartAndEndTime(lessonTime.getBegin(), lessonTime.getEnd()).ifPresent(obj -> {
            throw new LessonTimeNotUniqueException(String.format("Lesson time with begin %s and end %s already exist", lessonTime.getBegin(), lessonTime.getEnd()));
        });
        return true;
    }
}
