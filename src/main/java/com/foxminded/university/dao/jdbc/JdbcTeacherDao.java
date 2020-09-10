package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.mapper.SubjectMapper;
import com.foxminded.university.dao.mapper.TeacherMapper;
import com.foxminded.university.domain.Person;
import com.foxminded.university.domain.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class JdbcTeacherDao implements TeacherDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_TEACHER_BY_ID = "SELECT * FROM teachers WHERE teacher_id = ?";
    private static final String SQL_GET_ALL_TEACHERS = "SELECT * FROM teachers";
    private static final String SQL_SAVE_TEACHER = "INSERT INTO teachers VALUES (DEFAULT, ?)";
    private static final String SQL_UPDATE_TEACHER = "UPDATE teachers SET person_id = ? WHERE teacher_id = ?";
    private static final String SQL_DELETE_TEACHER = "DELETE FROM teachers WHERE teacher_id = ?";

    @Autowired
    public JdbcTeacherDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Teacher getById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_TEACHER_BY_ID, new TeacherMapper(new JdbcSubjectDao(jdbcTemplate.getDataSource())), id);
    }

    @Override
    public List<Teacher> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_TEACHERS, new TeacherMapper(new JdbcSubjectDao(jdbcTemplate.getDataSource())));
    }

    @Override
    public void save(Teacher teacher) {
        JdbcPersonDao jdbcPersonDao = new JdbcPersonDao(jdbcTemplate.getDataSource());
        Person person = new Person(teacher.getName(), teacher.getSurname(), teacher.getDateOfBirth(), teacher.getGender(),
                teacher.getEmail(), teacher.getPhoneNumber());
        jdbcPersonDao.save(person);
        teacher.setPersonId(person.getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_TEACHER, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, teacher.getPersonId());
            return statement;
        }, keyHolder);
        teacher.setId((int) keyHolder.getKeys().get("teacher_id"));
    }

    @Override
    public void update(Teacher teacher) {
        jdbcTemplate.update(SQL_UPDATE_TEACHER, teacher.getPersonId(), teacher.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_TEACHER, id);
    }
}
