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
        logger.debug("Saving subject: {}", subject);
        verifyCourseConsistent(subject.getCourse());
        verifySubjectUnique(subject.getName());
        subjectDao.save(subject);
    }

    public void update(Subject subject) {
        logger.debug("Updating subject by id: {}", subject);
        verifySubjectPresent(subject.getId());
        verifyCourseConsistent(subject.getCourse());
        subjectDao.update(subject);
    }

    public void delete(int id) {
        subjectDao.delete(id);
    }

    public List<Subject> getAllByTeacherId(int id) {
        return subjectDao.getAllByTeacherId(id);
    }

    private void verifySubjectPresent(int id) {
        subjectDao.getById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Subject with id %d is not present", id)));
    }

    private void verifySubjectUnique(String name) {
        subjectDao.getByName(name).ifPresent(obj -> {
            throw new SubjectNameNotUniqueException(String.format("Subject with name %s already exist", name));
        });
    }

    private void verifyCourseConsistent(int course) {
        if ((course > maxCourse) || (course < 1)) {
            throw new CourseNumberOutOfBoundsException("Course number is out of bounds");
        }
    }
}
