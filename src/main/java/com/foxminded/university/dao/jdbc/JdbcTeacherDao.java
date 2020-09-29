package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.jdbc.mapper.TeacherMapper;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Subject;
import com.foxminded.university.domain.Teacher;
import com.foxminded.university.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcTeacherDao implements TeacherDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcTeacherDao.class);

    private static final String SQL_GET_TEACHER_BY_ID = "SELECT * FROM teachers WHERE id = ?";
    private static final String SQL_GET_ALL_TEACHERS = "SELECT * FROM teachers";
    private static final String SQL_SAVE_TEACHER = "INSERT INTO teachers VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_TEACHER = "UPDATE teachers SET name = ?, surname = ?, date_of_birth = ?," +
            " gender = ?, email = ?, phone_number = ? WHERE id = ?";
    private static final String SQL_DELETE_TEACHER = "DELETE FROM teachers WHERE id = ?";
    private static final String SQL_DELETE_TEACHER_SUBJECT = "DELETE FROM teachers_subjects WHERE teacher_id = ? AND subject_id = ?";
    private static final String SQL_SAVE_TEACHER_SUBJECT = "INSERT INTO teachers_subjects VALUES (?, ?)";

    private final JdbcTemplate jdbcTemplate;
    private final TeacherMapper teacherMapper;
    private final SubjectDao subjectDao;

    public JdbcTeacherDao(JdbcTemplate jdbcTemplate, TeacherMapper teacherMapper, SubjectDao subjectDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.teacherMapper = teacherMapper;
        this.subjectDao = subjectDao;
    }

    @Override
    public Optional<Teacher> getById(int id) {
        logger.debug("Retrieving teacher with id {}", id);
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_GET_TEACHER_BY_ID, teacherMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            logger.error("Teacher with id {} is not present", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Teacher> getAll() {
        logger.debug("Retrieved all teachers");
        return jdbcTemplate.query(SQL_GET_ALL_TEACHERS, teacherMapper);
    }

    @Transactional
    @Override
    public void save(Teacher teacher) {
        logger.debug("Saving teacher");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_TEACHER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, teacher.getName());
            statement.setString(2, teacher.getSurname());
            statement.setObject(3, teacher.getDateOfBirth());
            statement.setString(4, teacher.getGender());
            statement.setString(5, teacher.getEmail());
            statement.setString(6, teacher.getPhoneNumber());
            return statement;
        }, keyHolder) == 0) {
            throw new EntityNotSavedException("Teacher was not saved");
        }
        teacher.setId((int) Objects.requireNonNull(keyHolder.getKeys()).get("id"));
        teacher.getSubjects().forEach(subject -> saveSubjectToTeacher(teacher.getId(), subject.getId()));
    }

    @Transactional
    @Override
    public void update(Teacher teacher) {
        logger.error("Updating teacher with id {}", teacher.getId());
        if (jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_TEACHER);
            statement.setString(1, teacher.getName());
            statement.setString(2, teacher.getSurname());
            statement.setObject(3, teacher.getDateOfBirth());
            statement.setString(4, teacher.getGender());
            statement.setString(5, teacher.getEmail());
            statement.setString(6, teacher.getPhoneNumber());
            statement.setInt(7, teacher.getId());

            List<Subject> oldSubjects = subjectDao.getAllByTeacherId(teacher.getId());
            oldSubjects.stream()
                    .filter(subject -> !teacher.getSubjects().contains(subject))
                    .forEach(subject -> removeSubjectFromTeacher(teacher.getId(), subject.getId()));

            teacher.getSubjects().stream()
                    .filter(subject -> !oldSubjects.contains(subject))
                    .forEach(subject -> saveSubjectToTeacher(teacher.getId(), subject.getId()));

            return statement;
        }) == 0) {
            throw new EntityNotUpdatedException(String.format("Teacher with id %d was not updated", teacher.getId()));
        }
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting teacher with id {}", id);
        if (jdbcTemplate.update(SQL_DELETE_TEACHER, id) == 0) {
            throw new EntityNotDeletedException(String.format("Teacher with id %d was not deleted", id));
        }
    }

    private void removeSubjectFromTeacher(int teacherId, int subjectId) {
        logger.debug("Removing subject with id {} form teacher with id {}", subjectId, teacherId);
        jdbcTemplate.update(SQL_DELETE_TEACHER_SUBJECT, teacherId, subjectId);
    }

    private void saveSubjectToTeacher(int teacherId, int subjectId) {
        logger.debug("Adding subject with id {} to teacher with id {}", subjectId, teacherId);
        jdbcTemplate.update(SQL_SAVE_TEACHER_SUBJECT, teacherId, subjectId);
    }
}
