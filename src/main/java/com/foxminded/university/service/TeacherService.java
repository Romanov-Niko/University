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
        logger.debug("Check consistency of teacher before updating");
        if (areSubjectsPresent(teacher.getSubjects())) {
            teacherDao.save(teacher);
        }
    }

    public void update(Teacher teacher) {
        logger.debug("Check consistency of teacher with id {} before updating", teacher.getId());
        if (isTeacherPresent(teacher.getId()) && areSubjectsPresent(teacher.getSubjects())) {
            teacherDao.update(teacher);
        }
    }

    public void delete(int id) {
        teacherDao.delete(id);
    }

    private boolean isTeacherPresent(int id) {
        return teacherDao.getById(id).map(obj -> true).orElseThrow(() -> new EntityNotFoundException(String.format("Teacher with id %d is not present", id)));
    }

    private boolean areSubjectsPresent(List<Subject> subjects) {
        subjects.forEach(subject -> subjectDao.getById(subject.getId())
                .map(obj -> true)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Subject with id %d is not present", subject.getId()))));
        return true;
    }
}
