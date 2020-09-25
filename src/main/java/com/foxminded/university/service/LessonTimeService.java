package com.foxminded.university.service;

import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.domain.LessonTime;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.exception.EntityOutOfBoundsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
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
        if (isDurationCorrect(lessonTime) && isLessonTimeUnique(lessonTime)) {
            lessonTimeDao.save(lessonTime);
        }
    }

    public void update(LessonTime lessonTime) {
        logger.debug("Check consistency of lesson time with id {} before updating", lessonTime.getId());
        if (isLessonTimePresent(lessonTime.getId()) && isDurationCorrect(lessonTime)) {
            lessonTimeDao.update(lessonTime);
        }
    }

    public void delete(int id) {
        lessonTimeDao.delete(id);
    }

    private boolean isLessonTimePresent(int id) {
        if (lessonTimeDao.getById(id).isPresent()) {
            logger.debug("Lesson time is present");
            return true;
        } else {
            throw new EntityNotFoundException("Lesson time is not present");
        }
    }

    private boolean isDurationCorrect (LessonTime lessonTime) {
        if (Duration.between(lessonTime.getBegin(), lessonTime.getEnd()).toMinutes() < maxLessonDuration
                && Duration.between(lessonTime.getBegin(), lessonTime.getEnd()).toMinutes() > 0) {
            logger.debug("Lesson time duration is consistent");
            return true;
        } else {
            throw new EntityOutOfBoundsException("Lesson time duration is out of bounds");
        }
    }

    private boolean isLessonTimeUnique(LessonTime lessonTime) {
        if (!lessonTimeDao.getByStartAndEndTime(lessonTime.getBegin(), lessonTime.getEnd()).isPresent()) {
            logger.debug("Lesson time is unique");
            return true;
        } else {
            throw new EntityNotUniqueException(String.format("Lesson time with begin %s and end %s already exist", lessonTime.getBegin(), lessonTime.getEnd()));
        }
    }
}
