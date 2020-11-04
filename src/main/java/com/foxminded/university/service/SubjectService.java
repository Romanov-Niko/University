package com.foxminded.university.service;

import com.foxminded.university.domain.Subject;
import com.foxminded.university.exception.CourseNumberOutOfBoundsException;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.SubjectNameNotUniqueException;
import com.foxminded.university.repository.SubjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);

    @Value("${maxCourse}")
    private int maxCourse;

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public Optional<Subject> findById(int id) {
        return subjectRepository.findById(id);
    }

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public void save(Subject subject) {
        logger.debug("Saving subject: {}", subject);
        verifyCourseConsistent(subject.getCourse());
        verifySubjectUnique(subject.getName());
        subjectRepository.save(subject);
    }

    public void update(Subject subject) {
        logger.debug("Updating subject by id: {}", subject);
        verifySubjectPresent(subject.getId());
        verifyCourseConsistent(subject.getCourse());
        subjectRepository.save(subject);
    }

    public void delete(int id) {
        subjectRepository.deleteById(id);
    }

    private void verifySubjectPresent(int id) {
        subjectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Subject with id %d is not present", id)));
    }

    private void verifySubjectUnique(String name) {
        subjectRepository.findByName(name).ifPresent(obj -> {
            throw new SubjectNameNotUniqueException(String.format("Subject with name %s already exist", name));
        });
    }

    private void verifyCourseConsistent(int course) {
        if ((course > maxCourse) || (course < 1)) {
            throw new CourseNumberOutOfBoundsException("Course number is out of bounds");
        }
    }
}
