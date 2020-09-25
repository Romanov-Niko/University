package com.foxminded.university.service;

import com.foxminded.university.dao.*;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Lesson;
import com.foxminded.university.exception.EntityBusyException;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityOutOfBoundsException;
import com.foxminded.university.exception.TeacherNotEnoughKnowledgesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LessonService {

    private static final Logger logger = LoggerFactory.getLogger(LessonService.class);

    private final LessonDao lessonDao;
    private final TeacherDao teacherDao;
    private final SubjectDao subjectDao;
    private final GroupDao groupDao;
    private final AudienceDao audienceDao;
    private final LessonTimeDao lessonTimeDao;

    public LessonService(LessonDao lessonDao, TeacherDao teacherDao, SubjectDao subjectDao, GroupDao groupDao, AudienceDao audienceDao, LessonTimeDao lessonTimeDao) {
        this.lessonDao = lessonDao;
        this.teacherDao = teacherDao;
        this.subjectDao = subjectDao;
        this.groupDao = groupDao;
        this.audienceDao = audienceDao;
        this.lessonTimeDao = lessonTimeDao;
    }

    public List<Lesson> getAll() {
        return lessonDao.getAll();
    }

    public void save(Lesson lesson) {
        logger.debug("Check consistency of lesson with id {} before saving", lesson.getId());
        if (isDataConsistent(lesson)) {
            lessonDao.save(lesson);
        }
    }

    public void update(Lesson lesson) {
        logger.debug("Check consistency of lesson with id {} before updating", lesson.getId());
        if (isLessonPresent(lesson.getId()) && isDataConsistent(lesson)) {
            lessonDao.update(lesson);
        }
    }

    public void delete(int id) {
        lessonDao.delete(id);
    }

    public List<Lesson> getAllByDate(LocalDate date) {
        return lessonDao.getAllByDate(date);
    }

    private boolean isLessonPresent(int id) {
        if (lessonDao.getById(id).isPresent()) {
            logger.debug("Lesson is present");
            return true;
        } else {
            throw new EntityNotFoundException("Lesson is not present");
        }
    }

    private boolean isTeacherPresent(int id) {
        if (teacherDao.getById(id).isPresent()) {
            logger.debug("Teacher is present");
            return true;
        } else {
            throw new EntityNotFoundException("Teacher is not present");
        }
    }

    private boolean isSubjectPresent(int id) {
        if (subjectDao.getById(id).isPresent()) {
            logger.debug("Subject is present");
            return true;
        } else {
            throw new EntityNotFoundException("Subject is not present");
        }
    }

    private boolean areGroupsPresent(List<Group> groups) {
        if (groups.stream().allMatch(group -> groupDao.getById(group.getId()).isPresent())) {
            logger.debug("All groups are present");
            return true;
        } else {
            throw new EntityNotFoundException("There are groups which are not present");
        }
    }

    private boolean isAudiencePresent(int id) {
        if (audienceDao.getById(id).isPresent()) {
            logger.debug("Audience is present");
            return true;
        } else {
            throw new EntityNotFoundException("Audience is not present");
        }
    }

    private boolean isLessonTimePresent(int id) {
        if (lessonTimeDao.getById(id).isPresent()) {
            logger.debug("Lesson time is present");
            return true;
        } else {
            throw new EntityNotFoundException("Lesson time is not present");
        }
    }

    private boolean isAudienceSuitable(Lesson lesson) {
        int numberOfStudents = lesson.getGroups().stream().mapToInt(group -> group.getStudents().size()).sum();
        if (lesson.getAudience().getCapacity() >= numberOfStudents) {
            logger.debug("Audience is spacious enough");
            return true;
        } else {
            throw new EntityOutOfBoundsException("Audience is too small");
        }
    }

    private boolean isTeacherSuitable(Lesson lesson) {
        if (lesson.getTeacher().getSubjects().contains(lesson.getSubject())) {
            logger.debug("Teacher can teach");
            return true;
        } else {
            throw new TeacherNotEnoughKnowledgesException(String.format("Teacher can not teach %s", lesson.getSubject()));
        }
    }

    private boolean isTeacherFree(Lesson currentLesson) {
        List<Lesson> lessons = lessonDao.getAllByTeacherIdDateAndLessonTimeId(currentLesson.getTeacher().getId(),
                currentLesson.getDate(), currentLesson.getLessonTime().getId());
        for (Lesson lesson : lessons) {
            if (lesson.getId() != currentLesson.getId()) {
                throw new EntityBusyException("Teacher is busy");
            }
        }
        logger.debug("Teacher is free");
        return true;
    }

    private boolean isAudienceFree(Lesson currentLesson) {
        List<Lesson> lessons = lessonDao.getAllByAudienceIdDateAndLessonTimeId(currentLesson.getAudience().getId(),
                currentLesson.getDate(), currentLesson.getLessonTime().getId());
        for (Lesson lesson : lessons) {
            if (lesson.getId() != currentLesson.getId()) {
                throw new EntityBusyException("Audience is busy");
            }
        }
        logger.debug("Audience is free");
        return true;
    }

    private boolean isDataConsistent(Lesson lesson) {
        return isSubjectPresent(lesson.getSubject().getId()) && isTeacherPresent(lesson.getTeacher().getId()) &&
                areGroupsPresent(lesson.getGroups()) && isAudiencePresent(lesson.getAudience().getId()) &&
                isLessonTimePresent(lesson.getLessonTime().getId()) && isAudienceSuitable(lesson) && isTeacherSuitable(lesson) &&
                isTeacherFree(lesson) && isAudienceFree(lesson);
    }
}
