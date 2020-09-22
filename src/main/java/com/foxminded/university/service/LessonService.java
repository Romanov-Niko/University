package com.foxminded.university.service;

import com.foxminded.university.dao.*;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Lesson;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {

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

    public void save(Lesson lesson, int dayScheduleId) {
        if (isDataConsistent(lesson, dayScheduleId)) {
            lessonDao.save(lesson);
        }
    }

    public void update(Lesson lesson, int dayScheduleId) {
        if (isLessonPresent(lesson.getId()) && isDataConsistent(lesson, dayScheduleId)) {
            lessonDao.update(lesson);
        }
    }

    public void delete(int id) {
        lessonDao.delete(id);
    }

    public List<Lesson> getAllByDayId(int id) {
        return lessonDao.getAllByDayId(id);
    }

    private boolean isLessonPresent(int id) {
        return lessonDao.getById(id).isPresent();
    }

    private boolean isTeacherPresent(int id) {
        return teacherDao.getById(id).isPresent();
    }

    private boolean isSubjectPresent(int id) {
        return subjectDao.getById(id).isPresent();
    }

    private boolean areGroupsPresent(List<Group> groups) {
        return groupDao.getAll().containsAll(groups);
    }

    private boolean isAudiencePresent(int id) {
        return audienceDao.getById(id).isPresent();
    }

    private boolean isLessonTimePresent(int id) {
        return lessonTimeDao.getById(id).isPresent();
    }

    private boolean isAudienceSuitable (Lesson lesson) {
        int numberOfStudents = lesson.getGroups().stream().mapToInt(group -> group.getStudents().size()).sum();
        return lesson.getAudience().getCapacity() >= numberOfStudents;
    }

    private boolean isTeacherSuitable(Lesson lesson) {
        return lesson.getTeacher().getSubjects().contains(lesson.getSubject());
    }

    private boolean isTeacherFree (Lesson currentLesson, int dayScheduleId) {
        return lessonDao.getAllByDayId(dayScheduleId).stream().noneMatch(lesson -> lesson.getTeacher().equals(currentLesson.getTeacher()));
    }

    private boolean isAudienceFree (Lesson currentLesson, int dayScheduleId) {
        return lessonDao.getAllByDayId(dayScheduleId).stream().noneMatch(lesson -> lesson.getAudience().equals(currentLesson.getAudience()));
    }

    private boolean isDataConsistent(Lesson lesson, int dayScheduleId) {
        return isSubjectPresent(lesson.getSubject().getId()) && isTeacherPresent(lesson.getTeacher().getId()) &&
                areGroupsPresent(lesson.getGroups()) && isAudiencePresent(lesson.getAudience().getId()) &&
                isLessonTimePresent(lesson.getLessonTime().getId()) && isAudienceSuitable(lesson) && isTeacherSuitable(lesson) &&
                isTeacherFree(lesson, dayScheduleId) && isAudienceFree(lesson, dayScheduleId);
    }
}
