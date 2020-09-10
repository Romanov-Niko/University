package com.foxminded.university.dao.jdbc;

import com.foxminded.university.EntitiesForTests;
import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.domain.Student;
import com.foxminded.university.domain.Subject;
import com.foxminded.university.domain.Teacher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@Sql({"/schema.sql", "/data.sql"})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationTestConfig.class})
class JdbcTeacherDaoTest {

    @Autowired
    private TeacherDao teacherDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void getById() {
        Teacher expectedTeacher = EntitiesForTests.teacherGetById;
        Teacher actualTeacher = teacherDao.getById(1);
        assertEquals(expectedTeacher, actualTeacher);
    }

    @Test
    void getAll() {
        int expectedRows = teacherDao.getAll().size();
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void save() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers") + 1;
        teacherDao.save(EntitiesForTests.teacherSave);
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void update() {
        Teacher teacherForUpdate = EntitiesForTests.teacherUpdate;
        teacherDao.update(teacherForUpdate);
        int updatedTeacher = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers", String.format(
                "person_id = %d AND teacher_id = %d", teacherForUpdate.getPersonId(), teacherForUpdate.getId()));
        assertEquals(1, updatedTeacher);
    }

    @Test
    void delete() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers") - 1;
        teacherDao.delete(3);
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers");
        assertEquals(expectedRows, actualRows);
    }
}