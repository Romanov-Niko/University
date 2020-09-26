package com.foxminded.university.service;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.domain.Subject;
import com.foxminded.university.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);

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
        logger.debug("Check consistency of subject with id {} before saving", subject.getId());
        if (isCourseConsistent(subject.getCourse()) && isSubjectUnique(subject.getName())) {
            subjectDao.save(subject);
        }
    }

    public void update(Subject subject) {
        logger.debug("Check consistency of subject with id {} before updating", subject.getId());
        if ((isSubjectPresent(subject.getId())) && isCourseConsistent(subject.getCourse())) {
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
        return subjectDao.getById(id).map(obj -> true).orElseThrow(() -> new EntityNotFoundException(String.format("Subject with id %d is not present", id)));
    }

    private boolean isSubjectUnique(String name) {
        subjectDao.getByName(name).ifPresent(obj -> {
            throw new SubjectNameNotUniqueException(String.format("Subject with name %s already exist", name));
        });
        return true;
    }

    private boolean isCourseConsistent(int course) {
        if ((course <= maxCourse) && (course > 0)) {
            return true;
        } else {
            throw new CourseNumberOutOfBoundsException("Course number is out of bounds");
        }
    }
}
