package com.foxminded.university.service;

import com.foxminded.university.dao.*;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Value("${maxGroupCapacity}")
    private int maxGroupCapacity;

    private final GroupDao groupDao;
    private final StudentDao studentDao;

    public GroupService(GroupDao groupDao, StudentDao studentDao) {
        this.groupDao = groupDao;
        this.studentDao = studentDao;
    }

    public List<Group> getAll() {
        return groupDao.getAll();
    }

    public void save(Group group) {
        if ((group.getStudents().size() <= maxGroupCapacity) && isGroupUnique(group.getName())) {
            groupDao.save(group);
        }
    }

    public void update(Group group) {
        if (isGroupPresent(group.getId()) && (group.getStudents().size() <= maxGroupCapacity) && areStudentsPresent(group.getStudents())) {
            groupDao.update(group);
        }
    }

    public void delete(int id) {
        groupDao.delete(id);
    }

    public List<Group> getAllByLessonId(int id) {
        return groupDao.getAllByLessonId(id);
    }

    private boolean isGroupPresent(int id) {
        return groupDao.getById(id).isPresent();
    }

    private boolean isGroupUnique(String name) {
        return groupDao.getAll().stream().noneMatch(group -> group.getName().equals(name));
    }

    private boolean areStudentsPresent(List<Student> students) {
        return studentDao.getAll().containsAll(students);
    }
}
