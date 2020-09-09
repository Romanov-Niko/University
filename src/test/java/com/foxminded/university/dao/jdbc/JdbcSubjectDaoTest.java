package com.foxminded.university.dao.jdbc;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.Subject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@Sql({"/schema.sql", "/data.sql"})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationTestConfig.class})
class JdbcSubjectDaoTest {

    @Autowired
    private SubjectDao subjectDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void getById() {
        Subject expectedSubject = new Subject(1, "Calculus", 120, 1, "math");
        Subject actualSubject = subjectDao.getById(1);
        assertEquals(expectedSubject, actualSubject);
    }

    @Test
    void getAll() {
        int expectedRows = subjectDao.getAll().size();
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void save() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects") + 1;
        subjectDao.save(new Subject("NEW", 120, 1, "math"));
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void update() {
        Subject subjectForUpdate = new Subject(1, "updated", 120, 1, "math");
        subjectDao.update(subjectForUpdate);
        int updatedAudience = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "subjects", String.format(
                "subject_id = %d AND name = '%s' AND credit_hours = %d AND specialty = '%s'",
                subjectForUpdate.getId(), subjectForUpdate.getName(), subjectForUpdate.getCreditHours(), subjectForUpdate.getSpecialty()
        ));
        assertEquals(1, updatedAudience);
    }

    @Test
    void delete() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects") - 1;
        subjectDao.delete(3);
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects");
        assertEquals(expectedRows, actualRows);
    }
}