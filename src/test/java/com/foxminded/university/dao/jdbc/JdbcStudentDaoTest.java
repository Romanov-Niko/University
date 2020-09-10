package com.foxminded.university.dao.jdbc;

import com.foxminded.university.EntitiesForTests;
import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.PersonDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.domain.Person;
import com.foxminded.university.domain.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Sql({"/schema.sql", "/data.sql"})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationTestConfig.class})
class JdbcStudentDaoTest {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void getById() {
        Student expectedStudent = EntitiesForTests.studentGetById;
        Student actualStudent = studentDao.getById(1);
        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void getAll() {
        int expectedRows = studentDao.getAll().size();
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void save() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students") + 1;
        studentDao.save(EntitiesForTests.studentSave);
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void update() {
        Student studentForUpdate = EntitiesForTests.studentUpdate;
        studentDao.update(studentForUpdate);
        int updatedStudent = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students", String.format(
                "person_id = %d AND student_id = %d AND group_id = %d AND specialty = '%s' AND course = %d " +
                        "AND admission = '%s' AND graduation = '%s'", studentForUpdate.getPersonId(), studentForUpdate.getId(),
                studentForUpdate.getGroupId(), studentForUpdate.getSpecialty(), studentForUpdate.getCourse(),
                studentForUpdate.getAdmission(), studentForUpdate.getGraduation()));
        assertEquals(1, updatedStudent);
    }

    @Test
    void delete() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students") - 1;
        studentDao.delete(3);
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void getAllByGroup() {
        List<Student> actualStudents = studentDao.getAllByGroup(1);
        assertEquals(1, actualStudents.size());
    }
}