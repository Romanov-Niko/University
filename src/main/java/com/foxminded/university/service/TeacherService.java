package com.foxminded.university.service;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.domain.Subject;
import com.foxminded.university.domain.Teacher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

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
        if (areSubjectsPresent(teacher.getSubjects())) {
            teacherDao.save(teacher);
        }
    }

    public void update(Teacher teacher) {
        if(isTeacherPresent(teacher.getId()) && areSubjectsPresent(teacher.getSubjects())) {
            teacherDao.update(teacher);
        }
    }

    public void delete(int id) {
        teacherDao.delete(id);
    }

    private boolean isTeacherPresent(int id) {
        return teacherDao.getById(id).isPresent();
    }

    private boolean areSubjectsPresent(List<Subject> subjects) {
        return subjectDao.getAll().containsAll(subjects);
    }
}
