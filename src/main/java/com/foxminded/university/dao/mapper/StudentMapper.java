package com.foxminded.university.dao.mapper;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.domain.Student;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet resultSet, int i) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getInt("student_id"));
        student.setPersonId(resultSet.getInt("person_id"));
        student.setGroupId(resultSet.getInt("group_id"));
        student.setSpecialty(resultSet.getString("specialty"));
        student.setCourse(resultSet.getInt("course"));
        student.setAdmission(resultSet.getDate("admission").toLocalDate());
        student.setGraduation(resultSet.getDate("graduation").toLocalDate());
        return student;
    }
}
