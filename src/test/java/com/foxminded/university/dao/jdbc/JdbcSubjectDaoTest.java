package com.foxminded.university.dao.jdbc;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.domain.Subject;
import com.foxminded.university.domain.Teacher;
import com.foxminded.university.service.SubjectService;
import com.foxminded.university.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringJUnitConfig(ApplicationTestConfig.class)
class JdbcSubjectDaoTest {

    @Autowired
    private SubjectDao subjectDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SubjectService subjectService;

    @Test
    void givenId1_whenGetById_thenReturnedFirstSubject() {
        Subject actualSubject = subjectDao.getById(1).orElse(null);
        subjectService.save(retrievedSubject);
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
}