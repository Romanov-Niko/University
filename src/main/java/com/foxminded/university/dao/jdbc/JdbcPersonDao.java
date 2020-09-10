package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.PersonDao;
import com.foxminded.university.dao.mapper.LessonTimeMapper;
import com.foxminded.university.dao.mapper.PersonMapper;
import com.foxminded.university.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Date;
import java.util.List;

@Component
public class JdbcPersonDao implements PersonDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_PERSON_BY_ID = "SELECT * FROM persons WHERE person_id = ?";
    private static final String SQL_GET_ALL_PERSONS = "SELECT * FROM persons";
    private static final String SQL_SAVE_PERSON = "INSERT INTO persons VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_PERSON = "UPDATE persons SET name = ?, surname = ?, date_of_birth = ?, gender = ?, email = ?, phone_number = ? WHERE person_id = ?";
    private static final String SQL_DELETE_PERSON = "DELETE FROM persons WHERE person_id = ?";

    @Autowired
    public JdbcPersonDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Person getById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_PERSON_BY_ID, new PersonMapper(), id);
    }

    @Override
    public List<Person> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_PERSONS, new PersonMapper());
    }

    @Override
    public void save(Person person) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_PERSON, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, person.getName());
            statement.setString(2, person.getSurname());
            statement.setDate(3, Date.valueOf(person.getDateOfBirth()));
            statement.setString(4, person.getGender());
            statement.setString(5, person.getEmail());
            statement.setString(6, person.getPhoneNumber());
            return statement;
        }, keyHolder);
        person.setId((int) keyHolder.getKeys().get("person_id"));
    }

    @Override
    public void update(Person person) {
        jdbcTemplate.update(SQL_UPDATE_PERSON, person.getName(), person.getSurname(), person.getDateOfBirth(),
                person.getGender(), person.getEmail(), person.getPhoneNumber(), person.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_PERSON, id);
    }
}
