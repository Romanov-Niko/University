package com.foxminded.university.service;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.domain.Subject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectService {

    private final SubjectDao subjectDao;
    private final TeacherDao teacherDao;

    public SubjectService(SubjectDao subjectDao, TeacherDao teacherDao) {
        this.subjectDao = subjectDao;
        this.teacherDao = teacherDao;
    }

    public List<Subject> getAll() {
        return subjectDao.getAll();
    }

    public void save(Subject subject) {
        if ((subject.getCourse() < 7) && isSubjectUnique(subject.getName())) {
            subjectDao.save(subject);
        }
    }

    public void update(Subject subject) {
        if ((isSubjectPresent(subject.getId())) && (subject.getCourse() < 7)) {
            subjectDao.update(subject);
        }
    }

    public void delete(int id) {
        subjectDao.delete(id);
    }

    public List<Subject> getAllByTeacherId(int id) {
        return subjectDao.getAllByTeacherId(id);
    }

    private boolean isSubjectPresent(int id) {
        return subjectDao.getById(id).isPresent();
    }

    private boolean isSubjectUnique(String name) {
        return subjectDao.getAll().stream().noneMatch(subject -> subject.getName().equals(name));
    }
}
