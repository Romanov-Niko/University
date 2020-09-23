package com.foxminded.university.service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.domain.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

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
        if ((student.getCourse() <= maxCourse) && (student.getCourse() > 0)) {
            studentDao.save(student);
        }
    }

    public void update(Student student) {
        if (isStudentPresent(student.getId()) && (student.getCourse() > 0) && (student.getCourse() <= maxCourse)) {
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
        return studentDao.getById(id).isPresent();
    }
}
