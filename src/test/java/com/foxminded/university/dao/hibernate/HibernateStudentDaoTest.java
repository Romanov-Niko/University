package com.foxminded.university.dao.hibernate;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.Lesson;
import com.foxminded.university.domain.LessonTime;
import com.foxminded.university.domain.Student;
import org.hibernate.SessionFactory;
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
import static org.junit.jupiter.api.Assertions.assertNull;

@Transactional
@SpringJUnitConfig(ApplicationTestConfig.class)
class HibernateStudentDaoTest {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void givenId1_whenGetById_thenReturnedFirstStudent() {
        Student expectedStudent = sessionFactory.getCurrentSession().find(Student.class, 1);

        Student actualStudent = studentDao.getById(1).orElse(null);

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedListOfAllStudents() {
        List<Student> expectedStudents = sessionFactory.getCurrentSession().createNativeQuery(
                "SELECT * FROM students", Student.class).getResultList();

        List<Student> actualStudents = studentDao.getAll();

        assertEquals(expectedStudents, actualStudents);
    }

    @Test
    void givenStudent_whenSave_thenAddedGivenStudent() {
        studentDao.save(createdStudent);

        Student actualStudent = sessionFactory.getCurrentSession().find(Student.class, createdStudent.getId());

        assertEquals(createdStudent, actualStudent);
    }

    @Test
    void givenStudent_whenUpdate_thenUpdatedStudentWithEqualId() {
        studentDao.update(updatedStudent);

        Student actualStudent = sessionFactory.getCurrentSession().find(Student.class, updatedStudent.getId());

        assertEquals(updatedStudent, actualStudent);
    }

    @Test
    void givenId3_whenDelete_thenDeletedThirdStudent() {
        studentDao.delete(3);

        Student actualStudent = sessionFactory.getCurrentSession().find(Student.class, 3);

        assertNull(actualStudent);
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
    void givenEmptyTable_whenGetAll_thenReturnedEmptyList() {
        sessionFactory.getCurrentSession().createNativeQuery("DELETE FROM students").executeUpdate();

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