package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.mapper.GroupMapper;
import com.foxminded.university.dao.mapper.StudentMapper;
import com.foxminded.university.domain.Group;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
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

    private final JdbcTemplate jdbcTemplate;
    private final GroupMapper groupMapper;
    private final StudentMapper studentMapper;

    public JdbcGroupDao(JdbcTemplate jdbcTemplate, GroupMapper groupMapper, StudentMapper studentMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.groupMapper = groupMapper;
        this.studentMapper = studentMapper;
    }

    @Override
    public Group getById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_GROUP_BY_ID, groupMapper, id);
    }

    @Override
    public List<Group> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_GROUPS, groupMapper);
    }

    @Override
    public void save(Group group) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_GROUP, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, group.getName());
            return statement;
        }, keyHolder);
        group.setId((int) keyHolder.getKeys().get("id"));
        group.setStudents(new JdbcStudentDao(jdbcTemplate, studentMapper).getAllByGroupId(group.getId()));
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
}
