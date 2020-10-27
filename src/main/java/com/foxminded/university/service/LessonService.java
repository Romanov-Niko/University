package com.foxminded.university.service;

import com.foxminded.university.dao.*;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Lesson;
import com.foxminded.university.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    public Optional<Lesson> getById(int id) {
        return lessonDao.getById(id);
    }

    public List<Lesson> getAll() {
        return lessonDao.getAll();
    }

    public void save(Lesson lesson) {
        logger.debug("Saving lesson: {}", lesson);
        verifyDataConsistent(lesson);
        lessonDao.save(lesson);
    }

    public void update(Lesson lesson) {
        logger.debug("Updating lesson by id: {}", lesson);
        verifyLessonPresent(lesson.getId());
        verifyDataConsistent(lesson);
        lessonDao.update(lesson);
    }

    public void delete(int id) {
        lessonDao.delete(id);
    }

    public List<Lesson> getAllByDate(LocalDate date) {
        return lessonDao.getAllByDate(date);
    }

    private void verifyLessonPresent(int id) {
        lessonDao.getById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Lesson with id %d is not present", id)));
    }

    private void verifyTeacherPresent(int id) {
        teacherDao.getById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Teacher with id %d is not present", id)));
    }

    private void verifySubjectPresent(int id) {
        subjectDao.getById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Subject with id %d is not present", id)));
    }

    private void verifyGroupsPresent(List<Group> groups) {
        groups.forEach(group -> groupDao.getById(group.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Group with id %d is not present", group.getId()))));
    }

    private void verifyAudiencePresent(int id) {
        audienceDao.getById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Audience with id %d is not present", id)));
    }

    private void verifyLessonTimePresent(int id) {
        lessonTimeDao.getById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Lesson time with id %d is not present", id)));
    }

    private void verifyAudienceHasEnoughCapacity(Lesson lesson) {
        int numberOfStudents = lesson.getGroups().stream().mapToInt(group -> group.getStudents().size()).sum();
        if (lesson.getAudience().getCapacity() < numberOfStudents) {
            throw new NotEnoughAudienceCapacityException(String.format("Audience with id %d has capacity %d when need %d", lesson.getAudience().getId(),
                    lesson.getAudience().getCapacity(), numberOfStudents));
        }
    }

    private void verifyTeacherHasEnoughKnowledges(Lesson lesson) {
        if (!lesson.getTeacher().getSubjects().contains(lesson.getSubject())) {
            throw new TeacherNotEnoughKnowledgesException(String.format("Teacher with id %d can not teach %s", lesson.getTeacher().getId(), lesson.getSubject().getName()));
        }
    }

    private void verifyTeacherFree(Lesson currentLesson) {
        List<Lesson> lessons = lessonDao.getAllByTeacherIdDateAndLessonTimeId(currentLesson.getTeacher().getId(),
                currentLesson.getDate(), currentLesson.getLessonTime().getId());
        for (Lesson lesson : lessons) {
            if (lesson.getId() != currentLesson.getId()) {
                throw new TeacherLessonOverlapException(String.format("Teacher with id %d is busy", currentLesson.getTeacher().getId()));
            }
        }
    }

    private void verifyAudienceFree(Lesson currentLesson) {
        List<Lesson> lessons = lessonDao.getAllByAudienceIdDateAndLessonTimeId(currentLesson.getAudience().getId(),
                currentLesson.getDate(), currentLesson.getLessonTime().getId());
        for (Lesson lesson : lessons) {
            if (lesson.getId() != currentLesson.getId()) {
                throw new AudienceLessonOverlapException(String.format("Audience with id %d is busy", currentLesson.getAudience().getId()));
            }
        }
    }

    private void verifyDataConsistent(Lesson lesson) {
        verifySubjectPresent(lesson.getSubject().getId());
        verifyTeacherPresent(lesson.getTeacher().getId());
        verifyGroupsPresent(lesson.getGroups());
        verifyAudiencePresent(lesson.getAudience().getId());
        verifyLessonTimePresent(lesson.getLessonTime().getId());
        verifyAudienceHasEnoughCapacity(lesson);
        verifyTeacherHasEnoughKnowledges(lesson);
        verifyTeacherFree(lesson);
        verifyAudienceFree(lesson);
    }
}
