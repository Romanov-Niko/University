package com.foxminded.university.service;

import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.domain.LessonTime;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.LessonDurationOutOfBoundsException;
import com.foxminded.university.exception.LessonTimeNotUniqueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class LessonTimeService {

    private static final Logger logger = LoggerFactory.getLogger(LessonTimeService.class);

    @Value("${maxLessonDuration}")
    private int maxLessonDuration;

    private final LessonTimeDao lessonTimeDao;

    public LessonTimeService(LessonTimeDao lessonTimeDao) {
        this.lessonTimeDao = lessonTimeDao;
    }

    public Optional<LessonTime> getById(int id) {
        return lessonTimeDao.getById(id);
    }

    public List<LessonTime> getAll() {
        return lessonTimeDao.getAll();
    }

    public void save(LessonTime lessonTime) {
        logger.debug("Saving lesson time: {}", lessonTime);
        verifyDurationConsistent(lessonTime);
        verifyLessonTimeUnique(lessonTime);
        lessonTimeDao.save(lessonTime);
    }

    public void update(LessonTime lessonTime) {
        logger.debug("Updating lesson time by id: {}", lessonTime);
        verifyLessonTimePresent(lessonTime.getId());
        verifyDurationConsistent(lessonTime);
        lessonTimeDao.update(lessonTime);
    }

    public void delete(int id) {
        lessonTimeDao.delete(id);
    }

    private void verifyLessonTimePresent(int id) {
        lessonTimeDao.getById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Lesson time with id %d is not present", id)));
    }

    private void verifyDurationConsistent(LessonTime lessonTime) {
        if (Duration.between(lessonTime.getBegin(), lessonTime.getEnd()).toMinutes() > maxLessonDuration
                || Duration.between(lessonTime.getBegin(), lessonTime.getEnd()).toMinutes() < 1) {
            throw new LessonDurationOutOfBoundsException("Lesson duration is out of bounds");
        }
    }

    private void verifyLessonTimeUnique(LessonTime lessonTime) {
        lessonTimeDao.getByStartAndEndTime(lessonTime.getBegin(), lessonTime.getEnd()).ifPresent(obj -> {
            throw new LessonTimeNotUniqueException(String.format("Lesson time with begin %s and end %s already exist", lessonTime.getBegin(), lessonTime.getEnd()));
        });
    }
}
