package com.foxminded.university.service;

import com.foxminded.university.dao.*;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Student;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.exception.EntityOutOfBoundsException;
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
        if (isGroupSizeConsistent(group.getStudents().size()) && isGroupUnique(group.getName()) && areStudentsPresent(group.getStudents())) {
            groupDao.save(group);
        }
    }

    public void update(Group group) {
        logger.debug("Check consistency of group with id {} before updating", group.getId());
        if (isGroupPresent(group.getId()) && isGroupSizeConsistent(group.getStudents().size()) && areStudentsPresent(group.getStudents())) {
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
        if (groupDao.getById(id).isPresent()) {
            logger.debug("Group is present");
            return true;
        } else {
            throw new EntityNotFoundException("Group is not present");
        }
    }

    private boolean isGroupUnique(String name) {
         if (!groupDao.getByName(name).isPresent()) {
             logger.debug("Group is unique");
             return true;
         } else {
             throw new EntityNotUniqueException(String.format("Group with name %s already exist", name));
         }
    }

    private boolean areStudentsPresent(List<Student> students) {
        if(students.stream().allMatch(student -> studentDao.getById(student.getId()).isPresent())) {
            logger.debug("All students are present");
            return true;
        } else {
            throw new EntityNotFoundException("There are students who are not present");
        }
    }

    private boolean isGroupSizeConsistent(int size) {
        if (size<=maxGroupCapacity) {
            logger.debug("Group capacity is consistent");
            return true;
        } else {
            throw new EntityOutOfBoundsException("Too many students in the group");
        }
    }
}
