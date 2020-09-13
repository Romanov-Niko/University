package com.foxminded.university.dao.mapper;

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
        student.setId(resultSet.getInt("id"));
        student.setGroupId(resultSet.getInt("group_id"));
        student.setSpecialty(resultSet.getString("specialty"));
        student.setCourse(resultSet.getInt("course"));
        student.setAdmission(resultSet.getDate("admission").toLocalDate());
        student.setGraduation(resultSet.getDate("graduation").toLocalDate());
        student.setName(resultSet.getString("name"));
        student.setSurname(resultSet.getString("surname"));
        student.setDateOfBirth(resultSet.getDate("date_of_birth").toLocalDate());
        student.setGender(resultSet.getString("gender"));
        student.setEmail(resultSet.getString("email"));
        student.setPhoneNumber(resultSet.getString("phone_number"));
        return student;
    }
}
