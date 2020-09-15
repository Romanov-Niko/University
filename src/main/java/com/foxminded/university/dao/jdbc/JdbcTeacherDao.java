package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.mapper.SubjectMapper;
import com.foxminded.university.dao.mapper.TeacherMapper;
import com.foxminded.university.domain.Subject;
import com.foxminded.university.domain.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class JdbcTeacherDao implements TeacherDao {

    private static final String SQL_GET_TEACHER_BY_ID = "SELECT * FROM teachers WHERE id = ?";
    private static final String SQL_GET_ALL_TEACHERS = "SELECT * FROM teachers";
    private static final String SQL_SAVE_TEACHER = "INSERT INTO teachers VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_TEACHER = "UPDATE teachers SET name = ?, surname = ?, date_of_birth = ?," +
            " gender = ?, email = ?, phone_number = ? WHERE id = ?";
    private static final String SQL_DELETE_TEACHER = "DELETE FROM teachers WHERE id = ?";
    private static final String SQL_DELETE_TEACHER_SUBJECTS = "DELETE FROM teachers_subjects WHERE teacher_id = ?";
    private static final String SQL_SAVE_TEACHER_SUBJECT = "INSERT INTO teachers_subjects VALUES (?, ?)";

    private final JdbcTemplate jdbcTemplate;
    private final TeacherMapper teacherMapper;

    public JdbcTeacherDao(JdbcTemplate jdbcTemplate, TeacherMapper teacherMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.teacherMapper = teacherMapper;
    }

    @Override
    public Teacher getById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_TEACHER_BY_ID, teacherMapper, id);
    }

    @Override
    public List<Teacher> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_TEACHERS, teacherMapper);
    }

    @Override
    public void save(Teacher teacher) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_TEACHER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, teacher.getName());
            statement.setString(2, teacher.getSurname());
            statement.setObject(3, teacher.getDateOfBirth());
            statement.setString(4, teacher.getGender());
            statement.setString(5, teacher.getEmail());
            statement.setString(6, teacher.getPhoneNumber());
            return statement;
        }, keyHolder);
        teacher.setId((int) keyHolder.getKeys().get("id"));
        saveSubjectsToTeacher(teacher.getId(), teacher.getSubjects());
    }

    @Override
    public void update(Teacher teacher) {
        jdbcTemplate.update(SQL_UPDATE_TEACHER, teacher.getName(), teacher.getSurname(), teacher.getDateOfBirth(),
                teacher.getGender(), teacher.getEmail(), teacher.getPhoneNumber(), teacher.getId());
        removeSubjectsByTeacherId(teacher.getId());
        saveSubjectsToTeacher(teacher.getId(), teacher.getSubjects());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_TEACHER, id);
    }

    private void removeSubjectsByTeacherId (int id) {
        jdbcTemplate.update(SQL_DELETE_TEACHER_SUBJECTS, id);
    }

    private void saveSubjectsToTeacher (int teacherId, List<Subject> subjects) {
        subjects.forEach(subject -> jdbcTemplate.update(SQL_SAVE_TEACHER_SUBJECT, teacherId, subject.getId()));
    }
}
