package com.foxminded.university.dao.hibernate;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.domain.Student;
import com.foxminded.university.domain.Subject;
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
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Transactional
@SpringJUnitConfig(ApplicationTestConfig.class)
class HibernateSubjectDaoTest {

    @Autowired
    private SubjectDao subjectDao;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void givenId1_whenGetById_thenReturnedFirstSubject() {
        Subject expectedSubject = sessionFactory.getCurrentSession().find(Subject.class, 1);

        Subject actualSubject = subjectDao.getById(1).orElse(null);

        assertEquals(expectedSubject, actualSubject);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedListOfAllSubjects() {
        List<Subject> expectedSubjects = sessionFactory.getCurrentSession().createNativeQuery(
                "SELECT * FROM subjects", Subject.class).getResultList();

        List<Subject> actualSubjects = subjectDao.getAll();

        assertEquals(expectedSubjects, actualSubjects);
    }

    @Test
    void givenSubject_whenSave_thenAddedGivenSubject() {
        subjectDao.save(createdSubject);

        Subject actualSubject = sessionFactory.getCurrentSession().find(Subject.class, createdSubject.getId());

        assertEquals(createdSubject, actualSubject);
    }

    @Test
    void givenSubject_whenUpdate_thenUpdatedSubjectWithEqualId() {
        subjectDao.update(updatedSubject);

        Subject actualSubject = sessionFactory.getCurrentSession().find(Subject.class, updatedSubject.getId());

        assertEquals(updatedSubject, actualSubject);
    }

    @Test
    void givenId3_whenDelete_thenDeletedThirdSubject() {
        subjectDao.delete(3);

        Subject actualSubject = sessionFactory.getCurrentSession().find(Subject.class, 3);

        assertNull(actualSubject);
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
    void givenEmptyTable_whenGetAll_thenReturnedEmptyList() {
        sessionFactory.getCurrentSession().createNativeQuery("DELETE FROM subjects").executeUpdate();

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