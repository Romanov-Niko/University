package com.foxminded.university.dao.hibernate;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.domain.Subject;
import com.foxminded.university.domain.Teacher;
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
class HibernateTeacherDaoTest {

    @Autowired
    private TeacherDao teacherDao;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void givenId1_whenGetById_thenReturnedFirstTeacher() {
        Teacher expectedTeacher = sessionFactory.getCurrentSession().find(Teacher.class, 1);

        Teacher actualTeacher = teacherDao.getById(1).orElse(null);

        assertEquals(expectedTeacher, actualTeacher);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedListOfAllTeachers() {
        List<Teacher> expectedTeachers = sessionFactory.getCurrentSession().createNativeQuery(
                "SELECT * FROM teachers", Teacher.class).getResultList();

        List<Teacher> actualTeachers = teacherDao.getAll();

        assertEquals(expectedTeachers, actualTeachers);
    }

    @Test
    void givenTeacher_whenSave_thenAddedGivenTeacher() {
        teacherDao.save(createdTeacher);

        Teacher actualTeacher = sessionFactory.getCurrentSession().find(Teacher.class, 4);

        assertEquals(createdTeacher, actualTeacher);
    }

    @Test
    void givenTeacher_whenUpdate_thenUpdatedTeacherWithEqualId() {
        teacherDao.update(updatedTeacher);

        Teacher actualTeacher = sessionFactory.getCurrentSession().find(Teacher.class, 1);

        assertEquals(updatedTeacher, actualTeacher);
    }

    @Test
    void givenId3_whenDelete_thenDeletedThirdTeacher() {
        teacherDao.delete(3);

        Teacher actualTeacher = sessionFactory.getCurrentSession().find(Teacher.class, 3);

        assertNull(actualTeacher);
    }

    @Test
    void givenNonExistentId_whenGetById_thenReturnedOptionalEmpty() {
        Optional<Teacher> actualTeacher = teacherDao.getById(4);

        assertEquals(Optional.empty(), actualTeacher);
    }

    @Test
    void givenEmptyTable_whenGetAll_thenReturnedEmptyList() {
        sessionFactory.getCurrentSession().createNativeQuery("DELETE FROM teachers").executeUpdate();

        List<Teacher> actualTeacherts = teacherDao.getAll();

        assertEquals(emptyList(), actualTeacherts);
    }
}