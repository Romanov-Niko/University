package com.foxminded.university.service;

import com.foxminded.university.domain.Student;
import com.foxminded.university.exception.CourseNumberOutOfBoundsException;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.GraduationIsAfterAdmissionException;
import com.foxminded.university.repository.GroupRepository;
import com.foxminded.university.repository.StudentRepository;
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

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public StudentService(StudentRepository studentRepository, GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    public Optional<Student> findById(int id) {
        return studentRepository.findById(id);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public void save(Student student) {
        logger.debug("Saving student: {}", student);
        verifyCourseConsistent(student.getCourse());
        verifyGroupPresent(student.getGroupId());
        verifyGraduationIsAfterAdmission(student.getAdmission(), student.getGraduation());
        studentRepository.save(student);
    }

    public void update(Student student) {
        logger.debug("Updating student by id: {}", student);
        verifyStudentPresent(student.getId());
        verifyCourseConsistent(student.getCourse());
        verifyGroupPresent(student.getGroupId());
        verifyGraduationIsAfterAdmission(student.getAdmission(), student.getGraduation());
        studentRepository.save(student);
    }

    public void delete(int id) {
        studentRepository.deleteById(id);
    }

    public List<Student> findAllByGroupId(int id) {
        return studentRepository.findAllByGroupId(id);
    }

    private void verifyStudentPresent(int id) {
        studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Student with id %d is not present", id)));
    }

    private void verifyGroupPresent(int id) {
        groupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Group with id %d is not present", id)));
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
