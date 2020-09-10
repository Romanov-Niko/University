package com.foxminded.university.dao.jdbc;

import com.foxminded.university.EntitiesForTests;
import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.dao.PersonDao;
import com.foxminded.university.domain.LessonTime;
import com.foxminded.university.domain.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@Sql({"/schema.sql", "/data.sql"})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationTestConfig.class})
class JdbcPersonDaoTest {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void getById() {
        Person expectedPerson = EntitiesForTests.personGetById;
        Person actualPerson = personDao.getById(1);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    void getAll() {
        int expectedRows = personDao.getAll().size();
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "persons");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void save() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "persons") + 1;
        personDao.save(EntitiesForTests.personSave);
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "persons");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void update() {
        Person personForUpdate = EntitiesForTests.personUpdate;
        personDao.update(personForUpdate);
        int updatedPerson = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "persons", String.format(
                "person_id = %d AND name = '%s' AND surname = '%s' AND date_of_birth = '%s' AND gender = '%s' AND " +
                        "email = '%s' AND phone_number = '%s'", personForUpdate.getId(), personForUpdate.getName(),
                personForUpdate.getSurname(), personForUpdate.getDateOfBirth(), personForUpdate.getGender(),
                personForUpdate.getEmail(), personForUpdate.getPhoneNumber()));
        assertEquals(1, updatedPerson);
    }

    @Test
    void delete() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "persons") - 1;
        personDao.delete(3);
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "persons");
        assertEquals(expectedRows, actualRows);
    }
}