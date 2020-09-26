package com.foxminded.university.service;

import com.foxminded.university.dao.*;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Student;
import com.foxminded.university.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
        logger.debug("Check consistency of group before saving");
        if (isGroupSizeConsistent(group) && isGroupUnique(group.getName()) && areStudentsPresent(group.getStudents())) {
            groupDao.save(group);
        }
    }

    public void update(Group group) {
        logger.debug("Check consistency of group with id {} before updating", group.getId());
        if (isGroupPresent(group.getId()) && isGroupSizeConsistent(group) && areStudentsPresent(group.getStudents())) {
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
        return groupDao.getById(id).map(obj -> true).orElseThrow(() -> new EntityNotFoundException(String.format("Group with id %d is not present", id)));
    }

    private boolean isGroupUnique(String name) {
        groupDao.getByName(name).ifPresent(obj -> {
            throw new GroupNameNotUniqueException(String.format("Group with name %s already exist", name));
        });
        return true;
    }

    private boolean areStudentsPresent(List<Student> students) {
        students.forEach(student -> studentDao.getById(student.getId())
                .map(obj -> true)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Student with id %d is not present", student.getId()))));
        return true;
    }

    private boolean isGroupSizeConsistent(Group group) {
        if (group.getStudents().size() <= maxGroupCapacity) {
            return true;
        } else {
            throw new GroupSizeTooLargeException(String.format("Group with id %d have too many students", group.getId()));
        }
    }
}
