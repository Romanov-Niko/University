package com.foxminded.university.service;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.domain.Subject;
import com.foxminded.university.domain.Teacher;
import com.foxminded.university.exception.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    private final TeacherDao teacherDao;
    private final SubjectDao subjectDao;

    public TeacherService(TeacherDao teacherDao, SubjectDao subjectDao) {
        this.teacherDao = teacherDao;
        this.subjectDao = subjectDao;
    }

    public List<Teacher> getAll() {
        return teacherDao.getAll();
    }

    public void save(Teacher teacher) {
        logger.debug("Saving teacher: {}", teacher);
        verifySubjectsPresent(teacher.getSubjects());
        teacherDao.save(teacher);
    }

    public void update(Teacher teacher) {
        logger.debug("Updating teacher by id: {}", teacher);
        verifyTeacherPresent(teacher.getId());
        verifySubjectsPresent(teacher.getSubjects());
        teacherDao.update(teacher);
    }

    public void delete(int id) {
        teacherDao.delete(id);
    }

    private void verifyTeacherPresent(int id) {
        teacherDao.getById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Teacher with id %d is not present", id)));
    }

    private void verifySubjectsPresent(List<Subject> subjects) {
        subjects.forEach(subject -> subjectDao.getById(subject.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Subject with id %d is not present", subject.getId()))));
    }
}
