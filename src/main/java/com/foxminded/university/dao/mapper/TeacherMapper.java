package com.foxminded.university.dao.mapper;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.domain.Teacher;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TeacherMapper implements RowMapper<Teacher> {

    private final SubjectDao subjectDao;

    public TeacherMapper(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    @Override
    public Teacher mapRow(ResultSet resultSet, int i) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(resultSet.getInt("teacher_id"));
        teacher.setPersonId(resultSet.getInt("person_id"));
        teacher.setSubjects(subjectDao.getAllByTeacherId(teacher.getId()));
        return teacher;
    }
}
