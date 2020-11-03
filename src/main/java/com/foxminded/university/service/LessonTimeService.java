package com.foxminded.university.service;

import com.foxminded.university.repository.LessonTimeRepository;
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

    private final LessonTimeRepository lessonTimeRepository;

    public LessonTimeService(LessonTimeRepository lessonTimeRepository) {
        this.lessonTimeRepository = lessonTimeRepository;
    }

    public Optional<LessonTime> findById(int id) {
        return lessonTimeRepository.findById(id);
    }

    public List<LessonTime> findAll() {
        return (List<LessonTime>) lessonTimeRepository.findAll();
    }

    public void save(LessonTime lessonTime) {
        logger.debug("Saving lesson time: {}", lessonTime);
        verifyDurationConsistent(lessonTime);
        verifyLessonTimeUnique(lessonTime);
        lessonTimeRepository.save(lessonTime);
    }

    public void update(LessonTime lessonTime) {
        logger.debug("Updating lesson time by id: {}", lessonTime);
        verifyLessonTimePresent(lessonTime.getId());
        verifyDurationConsistent(lessonTime);
        lessonTimeRepository.save(lessonTime);
    }

    public void delete(int id) {
        lessonTimeRepository.deleteById(id);
    }

    private void verifyLessonTimePresent(int id) {
        lessonTimeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Lesson time with id %d is not present", id)));
    }

    private void verifyDurationConsistent(LessonTime lessonTime) {
        if (Duration.between(lessonTime.getBegin(), lessonTime.getEnd()).toMinutes() > maxLessonDuration
                || Duration.between(lessonTime.getBegin(), lessonTime.getEnd()).toMinutes() < 1) {
            throw new LessonDurationOutOfBoundsException("Lesson duration is out of bounds");
        }
    }

    private void verifyLessonTimeUnique(LessonTime lessonTime) {
        lessonTimeRepository.findByBeginAndEnd(lessonTime.getBegin(), lessonTime.getEnd()).ifPresent(obj -> {
            throw new LessonTimeNotUniqueException(String.format("Lesson time with begin %s and end %s already exist", lessonTime.getBegin(), lessonTime.getEnd()));
        });
    }
}
