package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.mapper.PersonMapper;
import com.foxminded.university.dao.mapper.StudentMapper;
import com.foxminded.university.domain.Student;
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
public class JdbcStudentDao implements StudentDao {

    JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_STUDENT_BY_ID = "SELECT * FROM students WHERE student_id = ?";
    private static final String SQL_GET_ALL_STUDENTS = "SELECT * FROM students";
    private static final String SQL_SAVE_STUDENT = "INSERT INTO students VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_STUDENT = "UPDATE students SET person_id = ?, group_id = ?, specialty = ?, course = ?, admission = ?, graduation = ? WHERE student_id = ?";
    private static final String SQL_DELETE_STUDENT = "DELETE FROM students WHERE student_id = ?";
    private static final String SQL_GET_ALL_STUDENTS_BY_GROUP = "SELECT * FROM students WHERE group_id = ?";

    @Autowired
    public JdbcStudentDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Student getById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_STUDENT_BY_ID, new StudentMapper(), id);
    }

    @Override
    public List<Student> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_STUDENTS, new StudentMapper());
    }

    @Override
    public void save(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_STUDENT, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, student.getPersonId());
            statement.setInt(2, student.getGroup().getId());
            statement.setString(3, student.getSpecialty());
            statement.setInt(4, student.getCourse());
            statement.setDate(5, Date.valueOf(student.getAdmission()));
            statement.setDate(6, Date.valueOf(student.getGraduation()));
            return statement;
        }, keyHolder);
        student.setId((int)keyHolder.getKeys().get("student_id"));
    }

    @Override
    public void update(Student student) {
        jdbcTemplate.update(SQL_UPDATE_STUDENT, student.getPersonId(), student.getGroup().getId(), student.getSpecialty(),
                student.getCourse(), Date.valueOf(student.getAdmission()), Date.valueOf(student.getGraduation()), student.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_STUDENT, id);
    }

    @Override
    public List<Student> getAllByGroup(int id) {
        return jdbcTemplate.query(SQL_GET_ALL_STUDENTS_BY_GROUP, new StudentMapper());
    }
}
