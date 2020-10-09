package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.jdbc.mapper.GroupMapper;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Student;
import com.foxminded.university.domain.Subject;
import com.foxminded.university.exception.EntityNotDeletedException;
import com.foxminded.university.exception.EntityNotSavedException;
import com.foxminded.university.exception.EntityNotUpdatedException;
import com.foxminded.university.exception.GroupIdNotUpdatedInStudentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcGroupDao implements GroupDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcGroupDao.class);

    private static final String SQL_GET_GROUP_BY_ID = "SELECT * FROM groups WHERE id = ?";
    private static final String SQL_GET_ALL_GROUPS = "SELECT * FROM groups";
    private static final String SQL_SAVE_GROUP = "INSERT INTO groups VALUES (DEFAULT, ?)";
    private static final String SQL_UPDATE_GROUP = "UPDATE groups SET name = ? WHERE id = ?";
    private static final String SQL_DELETE_GROUP = "DELETE FROM groups WHERE id = ?";
    private static final String SQL_GET_ALL_GROUPS_BY_LESSON_ID = "SELECT lessons_groups.lesson_id, groups.id, groups.name " +
            "FROM lessons_groups " +
            "LEFT JOIN lessons ON lessons_groups.lesson_id = lessons.id " +
            "LEFT JOIN groups ON lessons_groups.group_id = groups.id " +
            "WHERE lessons.id = ?";
    private static final String SQL_UPDATE_STUDENT_GROUP = "UPDATE students SET group_id = ? WHERE id = ?";
    private static final String SQL_GET_GROUP_BY_NAME = "SELECT * FROM groups WHERE name = ?";
    private static final String SQL_REMOVE_STUDENT_FROM_GROUP = "UPDATE students SET group_id = NULL WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final GroupMapper groupMapper;
    private final StudentDao studentDao;

    public JdbcGroupDao(JdbcTemplate jdbcTemplate, GroupMapper groupMapper, StudentDao studentDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.groupMapper = groupMapper;
        this.studentDao = studentDao;
    }

    @Override
    public Optional<Group> getById(int id) {
        logger.debug("Retrieving group with id {}", id);
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_GET_GROUP_BY_ID, groupMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            logger.error("Group with id {} is not present", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Group> getAll() {
        logger.debug("Retrieved all groups");
        return jdbcTemplate.query(SQL_GET_ALL_GROUPS, groupMapper);
    }

    @Transactional
    @Override
    public void save(Group group) {
        logger.debug("Saving group");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_GROUP, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, group.getName());
            return statement;
        }, keyHolder) == 0) {
            throw new EntityNotSavedException("Group was not saved");
        }
        group.setId((int) keyHolder.getKeys().get("id"));
        group.getStudents().forEach(student -> {
            updateStudentGroup(student.getId(), group.getId());
            student.setGroupId(group.getId());
        });
    }

    @Transactional
    @Override
    public void update(Group group) {
        logger.debug("Updating group with id {}", group.getId());
        List<Student> oldStudents = studentDao.getAllByGroupId(group.getId());
        oldStudents.stream()
                .filter(student -> !group.getStudents().contains(student))
                .forEach(student -> removeStudentFromGroup(student.getId()));

        group.getStudents().stream()
                .filter(student -> !oldStudents.contains(student)).
                forEach(student -> {
            updateStudentGroup(student.getId(), group.getId());
            student.setGroupId(group.getId());
        });
        if (jdbcTemplate.update(SQL_UPDATE_GROUP, group.getName(), group.getId()) == 0) {
            throw new EntityNotUpdatedException(String.format("Group with id %d was not updated", group.getId()));
        }
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting group with id {}", id);
        if (jdbcTemplate.update(SQL_DELETE_GROUP, id) == 0) {
            throw new EntityNotDeletedException(String.format("Group with id %d was not deleted", id));
        }
    }

    @Override
    public List<Group> getAllByLessonId(int id) {
        logger.debug("Retrieving groups related to lesson with id {}", id);
        return jdbcTemplate.query(SQL_GET_ALL_GROUPS_BY_LESSON_ID, groupMapper, id);
    }

    @Override
    public Optional<Group> getByName(String name) {
        logger.debug("Retrieving group with name {}", name);
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_GET_GROUP_BY_NAME, groupMapper, name));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    private void updateStudentGroup(int studentId, int groupId) {
        logger.debug("Updating group id to {} for student with id {}", groupId, studentId);
        if (jdbcTemplate.update(SQL_UPDATE_STUDENT_GROUP, groupId, studentId) == 0) {
            throw new GroupIdNotUpdatedInStudentException(String.format("Group id to %d for student with id %d was not updated", groupId, studentId));
        }
    }

    private void removeStudentFromGroup(int studentId) {
        logger.debug("Removing group id for student with id {}", studentId);
        jdbcTemplate.update(SQL_REMOVE_STUDENT_FROM_GROUP, studentId);
    }
}
