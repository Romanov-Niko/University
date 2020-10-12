package com.foxminded.university.dao.jdbc;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.domain.Subject;
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
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringJUnitConfig(ApplicationTestConfig.class)
class JdbcSubjectDaoTest {

    @Autowired
    private SubjectDao subjectDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenId1_whenGetById_thenReturnedFirstSubject() {
        Subject actualSubject = subjectDao.getById(1).orElse(null);

        assertEquals(retrievedSubject, actualSubject);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedListOfAllSubjects() {
        int expectedRows = subjectDao.getAll().size();

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenSubject_whenSave_thenAddedGivenSubject() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects") + 1;

        subjectDao.save(createdSubject);

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenSubject_whenUpdate_thenUpdatedSubjectWithEqualId() {
        subjectDao.update(updatedSubject);

        int actualNumber = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "subjects", String.format(
                "id = %d AND name = '%s' AND credit_hours = %d AND specialty = '%s'",
                updatedSubject.getId(), updatedSubject.getName(), updatedSubject.getCreditHours(), updatedSubject.getSpecialty()
        ));
        assertEquals(1, actualNumber);
    }

    @Test
    void givenId3_whenDelete_thenDeletedThirdSubject() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects") - 1;

        subjectDao.delete(3);

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenId1_whenGetAllByTeacherId_thenReturnedAllSubjectsOfFirstTeacher() {
        List<Subject> expectedSubjects = singletonList(retrievedSubject);

        List<Subject> actualSubjects = subjectDao.getAllByTeacherId(1);

        assertEquals(expectedSubjects, actualSubjects);
    }

    @Test
    void givenNameOfFirstSubject_whenGetByName_thenReturnedFirstSubject() {
        Subject actualSubject = subjectDao.getByName("Calculus").orElse(null);

        assertEquals(retrievedSubject, actualSubject);
    }

    @Test
    void givenNonExistentId_whenGetById_thenReturnedOptionalEmpty() {
        Optional<Subject> actualSubject = subjectDao.getById(4);

        assertEquals(Optional.empty(), actualSubject);
    }

    @Test
    void givenNonExistentTable_whenGetAll_thenReturnedEmptyList() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "subjects");

        List<Subject> actualSubjects = subjectDao.getAll();

        assertEquals(emptyList(), actualSubjects);
    }

    @Test
    void givenNonExistentTeacherId_whenGetAllByTeacherId_thenReturnedEmptyList() {
        List<Subject> actualSubjects = subjectDao.getAllByTeacherId(0);

        assertEquals(emptyList(), actualSubjects);
    }

    @Test
    void givenNonExistentName_whenGetByName_thenReturnedOptionalEmpty() {
        Optional<Subject> actualSubject = subjectDao.getByName("INCORRECT");

        assertEquals(Optional.empty(), actualSubject);
    }
}