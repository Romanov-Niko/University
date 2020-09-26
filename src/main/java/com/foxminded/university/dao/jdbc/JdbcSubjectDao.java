package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.jdbc.mapper.SubjectMapper;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Subject;
import com.foxminded.university.exception.EntityNotDeletedException;
import com.foxminded.university.exception.EntityNotSavedException;
import com.foxminded.university.exception.EntityNotUpdatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcSubjectDao implements SubjectDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcSubjectDao.class);

    private static final String SQL_GET_SUBJECT_BY_ID = "SELECT * FROM subjects WHERE id = ?";
    private static final String SQL_GET_ALL_SUBJECTS = "SELECT * FROM subjects";
    private static final String SQL_SAVE_SUBJECT = "INSERT INTO subjects VALUES (DEFAULT, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_SUBJECT = "UPDATE subjects SET name = ?, credit_hours = ?, course = ?, specialty = ? WHERE id = ?";
    private static final String SQL_DELETE_SUBJECT = "DELETE FROM subjects WHERE id = ?";
    private static final String SQL_GET_ALL_SUBJECTS_BY_TEACHER_ID = "SELECT teachers_subjects.teacher_id, subjects.id, subjects.name, " +
            "subjects.credit_hours, subjects.course, subjects.specialty " +
            "FROM teachers_subjects " +
            "LEFT JOIN teachers ON teachers_subjects.teacher_id = teachers.id " +
            "LEFT JOIN subjects ON teachers_subjects.subject_id = subjects.id " +
            "WHERE teachers.id = ?";
    private static final String SQL_GET_SUBJECT_BY_NAME = "SELECT * FROM subjects WHERE name = ?";

    private final JdbcTemplate jdbcTemplate;
    private final SubjectMapper subjectMapper;

    public JdbcSubjectDao(JdbcTemplate jdbcTemplate, SubjectMapper subjectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.subjectMapper = subjectMapper;
    }

    @Override
    public Optional<Subject> getById(int id) {
        logger.debug("Retrieving subject with id {}", id);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_SUBJECT_BY_ID, subjectMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            logger.error("Subject with id {} is not present", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Subject> getAll() {
        logger.debug("Retrieved all subjects");
        return jdbcTemplate.query(SQL_GET_ALL_SUBJECTS, subjectMapper);
    }

    @Override
    public void save(Subject subject) {
        logger.debug("Saving subject");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_SUBJECT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, subject.getName());
            statement.setInt(2, subject.getCreditHours());
            statement.setInt(3, subject.getCourse());
            statement.setString(4, subject.getSpecialty());
            return statement;
        }, keyHolder) == 0) {
            throw new EntityNotSavedException("Subject was not saved");
        }
        subject.setId((int) Objects.requireNonNull(keyHolder.getKeys()).get("id"));
    }

    @Override
    public void update(Subject subject) {
        logger.debug("Updating subject with id {}", subject.getId());
        if (jdbcTemplate.update(SQL_UPDATE_SUBJECT, subject.getName(), subject.getCreditHours(), subject.getCourse(), subject.getSpecialty(), subject.getId()) == 0) {
            throw new EntityNotUpdatedException(String.format("Subject with id %d was not updated", subject.getId()));
        }
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting subject with id {}", id);
        if (jdbcTemplate.update(SQL_DELETE_SUBJECT, id) == 0) {
            throw new EntityNotDeletedException(String.format("Subject with id %d was not deleted", id));
        }
    }

    @Override
    public List<Subject> getAllByTeacherId(int id) {
        logger.debug("Retrieving subjects related to teacher with id {}", id);
        return jdbcTemplate.query(SQL_GET_ALL_SUBJECTS_BY_TEACHER_ID, subjectMapper, id);
    }

    @Override
    public Optional<Subject> getByName(String name) {
        logger.debug("Retrieving subject with name {}", name);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_SUBJECT_BY_NAME, subjectMapper, name));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
