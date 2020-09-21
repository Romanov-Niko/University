package com.foxminded.university.service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.domain.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    private final StudentDao studentDao;
    private final GroupDao groupDao;

    public StudentService(StudentDao studentDao, GroupDao groupDao) {
        this.studentDao = studentDao;
        this.groupDao = groupDao;
    }

    public List<Student> getAll() {
        return studentDao.getAll();
    }

    public void save(Student student) {
        if (student.getCourse() < 7) {
            studentDao.save(student);
        }
    }

    public void update(Student student) {
        if (isStudentPresent(student.getId()) && (student.getCourse() < 7)) {
            studentDao.update(student);
        }
    }

    public void delete(int id) {
        studentDao.delete(id);
    }

    public List<Student> getAllByGroupId(int id) {
        if (isGroupPresent(id)) {
            return studentDao.getAllByGroupId(id);
        }
        return new ArrayList<>();
    }

    public List<Student> getAllByGroupName(String name) {
        return studentDao.getAllByGroupName(name);
    }

    private boolean isStudentPresent(int id) {
        return studentDao.getById(id).isPresent();
    }

    private boolean isGroupPresent(int id) {
        return groupDao.getById(id).isPresent();
    }
}
