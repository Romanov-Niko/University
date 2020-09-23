package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.jdbc.mapper.GroupMapper;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.DaySchedule;
import com.foxminded.university.domain.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcGroupDao implements GroupDao {

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

    private final JdbcTemplate jdbcTemplate;
    private final GroupMapper groupMapper;

    public JdbcGroupDao(JdbcTemplate jdbcTemplate, GroupMapper groupMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.groupMapper = groupMapper;
    }

    @Override
    public Optional<Group> getById(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_GET_GROUP_BY_ID, groupMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Group> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_GROUPS, groupMapper);
    }

    @Transactional
    @Override
    public void save(Group group) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_GROUP, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, group.getName());
            return statement;
        }, keyHolder);
        group.setId((int) keyHolder.getKeys().get("id"));
        group.getStudents().forEach(student -> {
            updateStudentGroup(student.getId(), group.getId());
            student.setGroupId(group.getId());
        });
    }

    @Override
    public void update(Group group) {
        jdbcTemplate.update(SQL_UPDATE_GROUP, group.getName(), group.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_GROUP, id);
    }

    @Override
    public List<Group> getAllByLessonId(int id) {
        return jdbcTemplate.query(SQL_GET_ALL_GROUPS_BY_LESSON_ID, groupMapper, id);
    }

    @Override
    public Optional<Group> getByName(String name) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_GET_GROUP_BY_NAME, groupMapper, name));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    private void updateStudentGroup(int studentId, int groupId) {
        jdbcTemplate.update(SQL_UPDATE_STUDENT_GROUP, groupId, studentId);
    }
}
