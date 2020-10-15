package com.foxminded.university.dao.jdbc;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.domain.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringJUnitConfig(ApplicationTestConfig.class)
class JdbcTeacherDaoTest {

    @Autowired
    private TeacherDao teacherDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenId1_whenGetById_thenReturnedFirstTeacher() {
        Teacher actualTeacher = teacherDao.getById(1).orElse(null);

        assertEquals(retrievedTeacher, actualTeacher);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedListOfAllTeachers() {
        int expectedRows = teacherDao.getAll().size();

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenTeacher_whenSave_thenAddedGivenTeacher() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers") + 1;

        teacherDao.save(createdTeacher);

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenTeacher_whenUpdate_thenUpdatedTeacherWithEqualId() {
        teacherDao.update(updatedTeacher);

        int actualNumber = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teachers", String.format(
                "id = %d AND name = '%s' AND surname = '%s' AND date_of_birth = '%s' AND gender = '%s'" +
                        "AND email = '%s' AND phone_number = '%s'", updatedTeacher.getId(), updatedTeacher.getName(), updatedTeacher.getSurname(),
                updatedTeacher.getDateOfBirth(), updatedTeacher.getGender(), updatedTeacher.getEmail(), updatedTeacher.getPhoneNumber()));

        assertEquals(1, actualNumber);
    }

    @Test
    void givenId3_whenDelete_thenDeletedThirdTeacher() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers") - 1;

        teacherDao.delete(3);

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenNonExistentId_whenGetById_thenReturnedOptionalEmpty() {
        Optional<Teacher> actualTeacher = teacherDao.getById(4);

        assertEquals(Optional.empty(), actualTeacher);
    }

    @Test
    void givenNonExistentTable_whenGetAll_thenReturnedEmptyList() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "teachers");

        List<Teacher> actualTeachers = teacherDao.getAll();

        assertEquals(emptyList(), actualTeachers);
    }
}