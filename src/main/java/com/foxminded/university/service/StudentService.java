package com.foxminded.university.service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.Student;
import com.foxminded.university.exception.CourseNumberOutOfBoundsException;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.GraduationIsAfterAdmissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Value("${maxCourse}")
    private int maxCourse;

    private final StudentDao studentDao;
    private final GroupDao groupDao;

    public StudentService(StudentDao studentDao, GroupDao groupDao) {
        this.studentDao = studentDao;
        this.groupDao = groupDao;
    }

    public Optional<Student> getById(int id) {
        return studentDao.getById(id);
    }

    public List<Student> getAll() {
        return studentDao.getAll();
    }

    public void save(Student student) {
        logger.debug("Saving student: {}", student);
        verifyCourseConsistent(student.getCourse());
        verifyGroupPresent(student.getGroupId());
        verifyGraduationIsAfterAdmission(student.getAdmission(), student.getGraduation());
        studentDao.save(student);
    }

    public void update(Student student) {
        logger.debug("Updating student by id: {}", student);
        verifyStudentPresent(student.getId());
        verifyCourseConsistent(student.getCourse());
        verifyGroupPresent(student.getGroupId());
        verifyGraduationIsAfterAdmission(student.getAdmission(), student.getGraduation());
        studentDao.update(student);
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

    private void verifyStudentPresent(int id) {
        studentDao.getById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Student with id %d is not present", id)));
    }

    private void verifyGroupPresent(int id) {
        groupDao.getById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Group with id %d is not present", id)));
    }

    private void verifyCourseConsistent(int course) {
        if ((course > maxCourse) || (course < 1)) {
            throw new CourseNumberOutOfBoundsException("Course number is out of bounds");
        }
    }

    private void verifyGraduationIsAfterAdmission(LocalDate admission, LocalDate graduation) {
        if (admission.isAfter(graduation)) {
            throw new GraduationIsAfterAdmissionException("Admission can not be after graduation");
        }
    }
}
