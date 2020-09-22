package com.foxminded.university.service;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.domain.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectService {

    @Value("${maxCourse}")
    private int maxCourse;

    private final SubjectDao subjectDao;

    public SubjectService(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    public List<Subject> getAll() {
        return subjectDao.getAll();
    }

    public void save(Subject subject) {
        System.out.println(maxCourse);
        if ((subject.getCourse() <= maxCourse) && isSubjectUnique(subject.getName())) {
            subjectDao.save(subject);
        }
    }

    public void update(Subject subject) {
        if ((isSubjectPresent(subject.getId())) && (subject.getCourse() <= maxCourse)) {
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
        return subjectDao.getAll().stream()
                .map(Subject::getName)
                .noneMatch(name::equals);
    }
}
