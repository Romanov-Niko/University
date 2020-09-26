package com.foxminded.university.service;

import com.foxminded.university.dao.*;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Lesson;
import com.foxminded.university.exception.*;
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
        return lessonDao.getById(id).map(obj -> true).orElseThrow(() -> new EntityNotFoundException(String.format("Lesson with id %d is not present", id)));
    }

    private boolean isTeacherPresent(int id) {
        return teacherDao.getById(id).map(obj -> true).orElseThrow(() -> new EntityNotFoundException(String.format("Teacher with id %d is not present", id)));
    }

    private boolean isSubjectPresent(int id) {
        return subjectDao.getById(id).map(obj -> true).orElseThrow(() -> new EntityNotFoundException(String.format("Subject with id %d is not present", id)));
    }

    private boolean areGroupsPresent(List<Group> groups) {
        groups.forEach(group -> groupDao.getById(group.getId())
                .map(obj -> true)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Group with id %d is not present", group.getId()))));
        return true;
    }

    private boolean isAudiencePresent(int id) {
        return audienceDao.getById(id).map(obj -> true).orElseThrow(() -> new EntityNotFoundException(String.format("Audience with id %d is not present", id)));
    }

    private boolean isLessonTimePresent(int id) {
        return lessonTimeDao.getById(id).map(obj -> true).orElseThrow(() -> new EntityNotFoundException(String.format("Lesson time with id %d is not present", id)));
    }

    private boolean isAudienceSuitable(Lesson lesson) {
        int numberOfStudents = lesson.getGroups().stream().mapToInt(group -> group.getStudents().size()).sum();
        if (lesson.getAudience().getCapacity() >= numberOfStudents) {
            return true;
        } else {
            throw new AudienceSizeToSmallException(String.format("Audience with id %d is too small", lesson.getAudience().getId()));
        }
    }

    private boolean isTeacherSuitable(Lesson lesson) {
        if (lesson.getTeacher().getSubjects().contains(lesson.getSubject())) {
            return true;
        } else {
            throw new TeacherNotEnoughKnowledgesException(String.format("Teacher with id %d can not teach %s", lesson.getTeacher().getId(), lesson.getSubject().getName()));
        }
    }

    private boolean isTeacherFree(Lesson currentLesson) {
        List<Lesson> lessons = lessonDao.getAllByTeacherIdDateAndLessonTimeId(currentLesson.getTeacher().getId(),
                currentLesson.getDate(), currentLesson.getLessonTime().getId());
        for (Lesson lesson : lessons) {
            if (lesson.getId() != currentLesson.getId()) {
                throw new TeacherLessonOverlapException(String.format("Teacher with id %d is busy", currentLesson.getTeacher().getId()));
            }
        }
        return true;
    }

    private boolean isAudienceFree(Lesson currentLesson) {
        List<Lesson> lessons = lessonDao.getAllByAudienceIdDateAndLessonTimeId(currentLesson.getAudience().getId(),
                currentLesson.getDate(), currentLesson.getLessonTime().getId());
        for (Lesson lesson : lessons) {
            if (lesson.getId() != currentLesson.getId()) {
                throw new AudienceLessonOverlapException(String.format("Audience with id %d is busy", currentLesson.getAudience().getId()));
            }
        }
        return true;
    }

    private boolean isDataConsistent(Lesson lesson) {
        return isSubjectPresent(lesson.getSubject().getId()) && isTeacherPresent(lesson.getTeacher().getId()) &&
                areGroupsPresent(lesson.getGroups()) && isAudiencePresent(lesson.getAudience().getId()) &&
                isLessonTimePresent(lesson.getLessonTime().getId()) && isAudienceSuitable(lesson) && isTeacherSuitable(lesson) &&
                isTeacherFree(lesson) && isAudienceFree(lesson);
    }
}
