package com.foxminded.university.dao.jdbc;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.domain.Student;
import com.foxminded.university.domain.Subject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringJUnitConfig(ApplicationTestConfig.class)
class JdbcStudentDaoTest {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenId1_whenGetById_thenReturnedFirstStudent() {
        Student actualStudent = studentDao.getById(1).orElse(null);

        assertEquals(retrievedStudent, actualStudent);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedListOfAllStudents() {
        int expectedRows = studentDao.getAll().size();

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenStudent_whenSave_thenAddedGivenStudent() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students") + 1;

        studentDao.save(createdStudent);

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenStudent_whenUpdate_thenUpdatedStudentWithEqualId() {
        studentDao.update(updatedStudent);

        int actualNumber = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "students", String.format(
                "id = %d AND group_id = %d AND specialty = '%s' AND course = %d AND admission = '%s' " +
                        "AND graduation = '%s' AND name = '%s' AND surname = '%s' AND date_of_birth = '%s' AND gender = '%s' " +
                        "AND email = '%s' AND phone_number = '%s'", updatedStudent.getId(), updatedStudent.getGroupId(),
                updatedStudent.getSpecialty(), updatedStudent.getCourse(), updatedStudent.getAdmission(),
                updatedStudent.getGraduation(), updatedStudent.getName(), updatedStudent.getSurname(),
                updatedStudent.getDateOfBirth(), updatedStudent.getGender(), updatedStudent.getEmail(), updatedStudent.getPhoneNumber()));
        assertEquals(1, actualNumber);
    }

    @Test
    void givenId3_whenDelete_thenDeletedThirdStudent() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students") - 1;

        studentDao.delete(3);

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenId1_whenGetAllByGroupId_thenReturnedAllStudentsOfFirstGroup() {
        List<Student> actualStudents = studentDao.getAllByGroupId(1);

        assertEquals(1, actualStudents.size());
    }

    @Test
    void givenFirstGroupName_whenGetAllByGroupName_thenReturnedAllStudentsOfFirstGroup() {
        List<Student> actualStudents = studentDao.getAllByGroupName("AA-11");

        assertEquals(1, actualStudents.size());
    }

    @Test
    void givenNonExistentId_whenGetById_thenReturnedOptionalEmpty() {
        Optional<Student> actualStudent = studentDao.getById(4);

        assertEquals(Optional.empty(), actualStudent);
    }

    @Test
    void givenNonExistentTable_whenGetAll_thenReturnedEmptyList() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "students");

        List<Student> actualStudents = studentDao.getAll();

        assertEquals(emptyList(), actualStudents);
    }

    @Test
    void givenNonExistentGroupId_whenGetAllByGroupId_thenReturnedEmptyList() {
        List<Student> actualStudents = studentDao.getAllByGroupId(0);

        assertEquals(emptyList(), actualStudents);
    }

    @Test
    void givenNonExistentName_whenGetAllByGroupName_thenReturnedEmptyList() {
        List<Student> actualStudents = studentDao.getAllByGroupName("INCORRECT");

        assertEquals(emptyList(), actualStudents);
    }
}