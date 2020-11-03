package com.foxminded.university.service;

import com.foxminded.university.repository.GroupRepository;
import com.foxminded.university.repository.StudentRepository;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Student;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.GroupNameNotUniqueException;
import com.foxminded.university.exception.GroupSizeTooLargeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    @Value("${maxGroupCapacity}")
    private int maxGroupCapacity;

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    public GroupService(GroupRepository groupRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    public Optional<Group> findById(int id) {
        return groupRepository.findById(id);
    }

    public List<Group> findAll() {
        return (List<Group>) groupRepository.findAll();
    }

    public void save(Group group) {
        logger.debug("Saving group: {}", group);
        verifyGroupSizeConsistent(group);
        verifyGroupUnique(group);
        verifyStudentsPresent(group.getStudents());
        groupRepository.save(group);
    }

    public void update(Group group) {
        logger.debug("Updating group by id: {}", group);
        verifyGroupPresent(group.getId());
        verifyGroupUnique(group);
        verifyGroupSizeConsistent(group);
        verifyStudentsPresent(group.getStudents());
        groupRepository.save(group);
    }

    public void delete(int id) {
        groupRepository.deleteById(id);
    }

    public List<Group> findAllByLessonId(int id) {
        return groupRepository.findAllByLessonId(id);
    }

    private void verifyGroupPresent(int id) {
        groupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Group with id %d is not present", id)));
    }

    private void verifyGroupUnique(Group group) {
        groupRepository.findByName(group.getName()).ifPresent(groupWithSameName -> {
            if (group.getId() != groupWithSameName.getId()) {
                throw new GroupNameNotUniqueException(String.format("Group with name %s already exist", group.getName()));
            }
        });
    }

    private void verifyStudentsPresent(List<Student> students) {
        students.forEach(student -> studentRepository.findById(student.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Student with id %d is not present", student.getId()))));
    }

    private void verifyGroupSizeConsistent(Group group) {
        if (group.getStudents().size() > maxGroupCapacity) {
            throw new GroupSizeTooLargeException(String.format("Group with id %d have too many students", group.getId()));
        }
    }
}
