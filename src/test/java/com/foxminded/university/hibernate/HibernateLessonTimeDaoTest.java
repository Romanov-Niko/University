package com.foxminded.university.hibernate;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Lesson;
import com.foxminded.university.domain.LessonTime;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Transactional
@SpringJUnitConfig(ApplicationTestConfig.class)
class HibernateLessonTimeDaoTest {

    @Autowired
    private LessonTimeDao lessonTimeDao;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void givenId1_whenGetById_thenReturnedFirstLessonTime() {
        LessonTime expectedLessonTime = sessionFactory.getCurrentSession().find(LessonTime.class, 1);

        LessonTime actualLessonTime = lessonTimeDao.getById(1).orElse(null);

        assertEquals(expectedLessonTime, actualLessonTime);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedListOfAllLessonsTimes() {
        List<LessonTime> expectedLessonsTimes = sessionFactory.getCurrentSession().createNativeQuery(
                "SELECT * FROM lessons_times", LessonTime.class).getResultList();

        List<LessonTime> actualLessonsTimes = lessonTimeDao.getAll();

        assertEquals(expectedLessonsTimes, actualLessonsTimes);
    }

    @Test
    void givenLessonTime_whenSave_thenAddedGivenLessonTime() {
        lessonTimeDao.save(createdLessonTime);

        LessonTime actualLessonTime = sessionFactory.getCurrentSession().find(LessonTime.class, createdLessonTime.getId());

        assertEquals(createdLessonTime, actualLessonTime);
    }

    @Test
    void givenLessonTime_whenUpdate_thenUpdatedLessonTimeWithEqualId() {
        lessonTimeDao.update(updatedLessonTime);

        LessonTime actualLessonTime = sessionFactory.getCurrentSession().find(LessonTime.class, updatedLessonTime.getId());

        assertEquals(updatedLessonTime, actualLessonTime);
    }

    @Test
    void givenId3_whenDelete_thenDeletedThirdLessonTime() {
        lessonTimeDao.delete(3);

        LessonTime actualLessonTime = sessionFactory.getCurrentSession().find(LessonTime.class, 3);

        assertNull(actualLessonTime);
    }

    @Test
    void given8AMAnd9AM_whenGetByStartAndEndTime_thenReturnedFirstLessonTime() {
        LessonTime actualLessonTime = lessonTimeDao.getByStartAndEndTime(LocalTime.parse("08:00:00"), LocalTime.parse("09:00:00")).orElse(null);

        assertEquals(retrievedLessonTime, actualLessonTime);
    }

    @Test
    void givenNonExistentId_whenGetById_thenReturnedOptionalEmpty() {
        Optional<LessonTime> actualLessonTime = lessonTimeDao.getById(4);

        assertEquals(Optional.empty(), actualLessonTime);
    }

    @Test
    void givenEmptyTable_whenGetAll_thenReturnedEmptyList() {
        sessionFactory.getCurrentSession().createNativeQuery("DELETE FROM lessons_times").executeUpdate();

        List<LessonTime> actualLessonsTimes = lessonTimeDao.getAll();

        assertEquals(emptyList(), actualLessonsTimes);
    }

    @Test
    void givenWrongTime_whenGetByStartAndEndTime_thenReturnedOptionalEmpty() {
        Optional<LessonTime> actualLessonTime = lessonTimeDao.getByStartAndEndTime(LocalTime.parse("01:00:00"), LocalTime.parse("02:00:00"));

        assertEquals(Optional.empty(), actualLessonTime);
    }
}