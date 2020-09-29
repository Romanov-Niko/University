package com.foxminded.university.service;

import com.foxminded.university.dao.*;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Student;
import com.foxminded.university.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

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
        logger.debug("Saving group: {}", group);
        verifyGroupSizeConsistent(group);
        verifyGroupUnique(group);
        verifyStudentsPresent(group.getStudents());
        groupDao.save(group);
    }

    public void update(Group group) {
        logger.debug("Updating group by id: {}", group);
        verifyGroupPresent(group.getId());
        verifyGroupUnique(group);
        verifyGroupSizeConsistent(group);
        verifyStudentsPresent(group.getStudents());
        groupDao.update(group);
    }

    public void delete(int id) {
        groupDao.delete(id);
    }

    public List<Group> getAllByLessonId(int id) {
        return groupDao.getAllByLessonId(id);
    }

    private void verifyGroupPresent(int id) {
        groupDao.getById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Group with id %d is not present", id)));
    }

    private void verifyGroupUnique(Group group) {
        groupDao.getByName(group.getName()).ifPresent(groupWithSameName -> {
            if (group.getId() != groupWithSameName.getId()) {
                throw new GroupNameNotUniqueException(String.format("Group with name %s already exist", group.getName()));
            }
        });
    }

    private void verifyStudentsPresent(List<Student> students) {
        students.forEach(student -> studentDao.getById(student.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Student with id %d is not present", student.getId()))));
    }

    private void verifyGroupSizeConsistent(Group group) {
        if (group.getStudents().size() > maxGroupCapacity) {
            throw new GroupSizeTooLargeException(String.format("Group with id %d have too many students", group.getId()));
        }
    }
}
