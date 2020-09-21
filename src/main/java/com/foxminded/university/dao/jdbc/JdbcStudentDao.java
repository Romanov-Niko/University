package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.jdbc.mapper.StudentMapper;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Student;
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
import java.util.Optional;

@Repository
public class JdbcStudentDao implements StudentDao {

    private static final String SQL_GET_STUDENT_BY_ID = "SELECT * FROM students WHERE id = ?";
    private static final String SQL_GET_ALL_STUDENTS = "SELECT * FROM students";
    private static final String SQL_SAVE_STUDENT = "INSERT INTO students VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_STUDENT = "UPDATE students SET group_id = ?, specialty = ?, course = ?, admission = ?, " +
            "graduation = ?, name = ?, surname = ?, date_of_birth = ?, gender = ?, email = ?, phone_number = ? WHERE id = ?";
    private static final String SQL_DELETE_STUDENT = "DELETE FROM students WHERE id = ?";
    private static final String SQL_GET_ALL_STUDENTS_BY_GROUP_ID = "SELECT * FROM students WHERE group_id = ?";
    private static final String SQL_GET_ALL_STUDENTS_BY_GROUP_NAME = "SELECT * FROM students " +
            "LEFT JOIN groups ON students.group_id = groups.id " +
            "WHERE groups.name = ?";

    private final JdbcTemplate jdbcTemplate;
    private final StudentMapper studentMapper;

    public JdbcStudentDao(JdbcTemplate jdbcTemplate, StudentMapper studentMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentMapper = studentMapper;
    }

    @Override
    public Optional<Student> getById(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_GET_STUDENT_BY_ID, studentMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Student> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_STUDENTS, studentMapper);
    }

    @Override
    public void save(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_STUDENT, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, student.getGroupId());
            statement.setString(2, student.getSpecialty());
            statement.setInt(3, student.getCourse());
            statement.setObject(4, student.getAdmission());
            statement.setObject(5, student.getGraduation());
            statement.setString(6, student.getName());
            statement.setString(7, student.getSurname());
            statement.setObject(8, student.getDateOfBirth());
            statement.setString(9, student.getGender());
            statement.setString(10, student.getEmail());
            statement.setString(11, student.getPhoneNumber());
            return statement;
        }, keyHolder);
        student.setId((int) keyHolder.getKeys().get("id"));
    }

    @Override
    public void update(Student student) {
        jdbcTemplate.update(SQL_UPDATE_STUDENT, student.getGroupId(), student.getSpecialty(), student.getCourse(),
                student.getAdmission(), student.getGraduation(), student.getName(),
                student.getSurname(), student.getDateOfBirth(), student.getGender(), student.getEmail(),
                student.getPhoneNumber(), student.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_STUDENT, id);
    }

    @Override
    public List<Student> getAllByGroupId(int id) {
        return jdbcTemplate.query(SQL_GET_ALL_STUDENTS_BY_GROUP_ID, studentMapper, id);
    }

    @Override
    public List<Student> getAllByGroupName(String name) {
        return jdbcTemplate.query(SQL_GET_ALL_STUDENTS_BY_GROUP_NAME, studentMapper, name);
    }
}
