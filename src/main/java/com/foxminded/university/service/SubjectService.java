package com.foxminded.university.service;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.domain.Subject;
import com.foxminded.university.exception.EntityOutOfBoundsException;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
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
        if (subjectDao.getById(id).isPresent()) {
            logger.debug("Subject is present");
            return true;
        } else {
            throw new EntityNotFoundException("Subject is not present");
        }
    }

    private boolean isSubjectUnique(String name) {
        if (!subjectDao.getByName(name).isPresent()) {
            logger.debug("Subject is unique");
            return true;
        } else {
            throw new EntityNotUniqueException(String.format("Subject with name %s already exist", name));
        }
    }

    private boolean isCourseConsistent(int course) {
        if ((course <= maxCourse) && (course > 0)) {
            logger.debug("Course is consistent");
            return true;
        } else {
            throw new EntityOutOfBoundsException("Course out of bounds");
        }
    }
}
