package com.foxminded.university.dao.mapper;

import com.foxminded.university.domain.Subject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SubjectMapper implements RowMapper<Subject> {

    @Override
    public Subject mapRow(ResultSet resultSet, int i) throws SQLException {
        Subject subject = new Subject();
        subject.setId(resultSet.getInt("subject_id"));
        subject.setName(resultSet.getString("name"));
        subject.setCreditHours(resultSet.getInt("credit_hours"));
        subject.setCourse(resultSet.getInt("course"));
        subject.setSpecialty(resultSet.getString("specialty"));
        return subject;
    }
}
