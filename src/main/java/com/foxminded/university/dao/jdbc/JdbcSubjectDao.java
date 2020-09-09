package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.mapper.StudentMapper;
import com.foxminded.university.dao.mapper.SubjectMapper;
import com.foxminded.university.domain.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class JdbcSubjectDao implements SubjectDao {

    JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_SUBJECT_BY_ID = "SELECT * FROM subjects WHERE subject_id = ?";
    private static final String SQL_GET_ALL_SUBJECTS = "SELECT * FROM subjects";
    private static final String SQL_SAVE_SUBJECT = "INSERT INTO subjects VALUES (DEFAULT, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_SUBJECT = "UPDATE subjects SET name = ?, credit_hours = ?, course = ?, specialty = ? WHERE subject_id = ?";
    private static final String SQL_DELETE_SUBJECT = "DELETE FROM subjects WHERE subject_id = ?";

    @Autowired
    public JdbcSubjectDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Subject getById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_SUBJECT_BY_ID, new SubjectMapper(), id);
    }

    @Override
    public List<Subject> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_SUBJECTS, new SubjectMapper());
    }

    @Override
    public void save(Subject subject) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_SUBJECT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, subject.getName());
            statement.setInt(2, subject.getCreditHours());
            statement.setInt(3, subject.getCourse());
            statement.setString(4, subject.getSpecialty());
            return statement;
        }, keyHolder);
        subject.setId((int)keyHolder.getKeys().get("subject_id"));
    }

    @Override
    public void update(Subject subject) {
        jdbcTemplate.update(SQL_UPDATE_SUBJECT, subject.getName(), subject.getCreditHours(), subject.getCourse(), subject.getSpecialty(), subject.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_SUBJECT, id);
    }
}
