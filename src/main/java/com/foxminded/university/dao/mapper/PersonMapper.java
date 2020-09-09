package com.foxminded.university.dao.mapper;

import com.foxminded.university.domain.Person;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PersonMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet resultSet, int i) throws SQLException {
        Person person = new Person();
        person.setId(resultSet.getInt("person_id"));
        person.setName(resultSet.getString("name"));
        person.setSurname(resultSet.getString("surname"));
        person.setDateOfBirth(resultSet.getDate("date_of_birth").toLocalDate());
        person.setGender(resultSet.getString("gender"));
        person.setEmail(resultSet.getString("email"));
        person.setPhoneNumber(resultSet.getString("phone_number"));
        return person;
    }
}
