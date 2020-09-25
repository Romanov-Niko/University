package com.foxminded.university.service;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.domain.Student;
import com.foxminded.university.exception.EntityOutOfBoundsException;
import com.foxminded.university.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Value("${maxCourse}")
    private int maxCourse;

    private final StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public List<Student> getAll() {
        return studentDao.getAll();
    }

    public void save(Student student) {
        logger.debug("Check consistency of student with id {} before saving", student.getId());
        if (isCourseConsistent(student.getCourse())) {
            studentDao.save(student);
        }
    }

    public void update(Student student) {
        logger.debug("Check consistency of student with id {} before updating", student.getId());
        if (isStudentPresent(student.getId()) && isCourseConsistent(student.getCourse())) {
            studentDao.update(student);
        }
    }

    public void delete(int id) {
        studentDao.delete(id);
    }

    public List<Student> getAllByGroupId(int id) {
        return studentDao.getAllByGroupId(id);
    }

    public List<Student> getAllByGroupName(String name) {
        return studentDao.getAllByGroupName(name);
    }

    private boolean isStudentPresent(int id) {
        if (studentDao.getById(id).isPresent()) {
            logger.debug("Student is present");
            return true;
        } else {
            throw new EntityNotFoundException("Student is not present");
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
