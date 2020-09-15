package com.foxminded.university.dao.mapper;

import com.foxminded.university.domain.Student;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class StudentMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet resultSet, int i) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getInt("id"));
        student.setGroupId(resultSet.getInt("group_id"));
        student.setSpecialty(resultSet.getString("specialty"));
        student.setCourse(resultSet.getInt("course"));
        student.setAdmission(resultSet.getObject("admission", LocalDate.class));
        student.setGraduation(resultSet.getObject("graduation", LocalDate.class));
        student.setName(resultSet.getString("name"));
        student.setSurname(resultSet.getString("surname"));
        student.setDateOfBirth(resultSet.getObject("date_of_birth", LocalDate.class));
        student.setGender(resultSet.getString("gender"));
        student.setEmail(resultSet.getString("email"));
        student.setPhoneNumber(resultSet.getString("phone_number"));
        return student;
    }
}
