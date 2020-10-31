package com.foxminded.university.hibernate;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.domain.DaySchedule;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Lesson;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringJUnitConfig(ApplicationTestConfig.class)
class HibernateLessonDaoTest {

    @Autowired
    private LessonDao lessonDao;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void givenId1_whenGetById_thenReturnedFirstGroup() {
        Lesson expectedLesson = sessionFactory.getCurrentSession().find(Lesson.class, 1);

        Lesson actualLesson = lessonDao.getById(1).orElse(null);

        assertEquals(expectedLesson, actualLesson);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedListOfAllLessons() {
        List<Lesson> expectedLessons = sessionFactory.getCurrentSession().createNativeQuery(
                "SELECT * FROM lessons", Lesson.class).getResultList();

        List<Lesson> actualLessons = lessonDao.getAll();

        assertEquals(expectedLessons, actualLessons);
    }

    @Test
    void givenLesson_whenSave_thenAddedGivenLesson() {
        lessonDao.save(createdLesson);

        Lesson actualLesson = sessionFactory.getCurrentSession().find(Lesson.class, createdLesson.getId());

        assertEquals(createdLesson, actualLesson);
    }

    @Test
    void givenLesson_whenUpdate_thenUpdatedLessonWithEqualId() {
        lessonDao.update(updatedLesson);

        Lesson actualLesson = sessionFactory.getCurrentSession().find(Lesson.class, updatedLesson.getId());

        assertEquals(updatedLesson, actualLesson);
    }

    @Test
    void givenId3_whenDelete_thenDeletedThirdLesson() {
        lessonDao.delete(3);

        Lesson actualLesson = sessionFactory.getCurrentSession().find(Lesson.class, 3);

        assertNull(actualLesson);
    }

    @Test
    void givenFirstDate_whenGetAllByDate_thenReturnedAllLessonsOfFirstDay() {
        List<Lesson> expectedLessons = singletonList(retrievedLesson);

        List<Lesson> actualLessons = lessonDao.getAllByDate(LocalDate.parse("2017-06-01"));

        assertEquals(expectedLessons, actualLessons);
    }

    @Test
    void givenFirstLessonData_whenGetAllByTeacherIdDateAndLessonTimeId_thenReturnedFirstLesson() {
        List<Lesson> actualLessons = lessonDao.getAllByTeacherIdDateAndLessonTimeId(1, LocalDate.parse("2017-06-01"), 1);

        assertEquals(singletonList(retrievedLesson), actualLessons);
    }

    @Test
    void givenFirstLessonData_whenGetAllByAudienceIdDateAndLessonTimeId_thenReturnedFirstLesson() {
        List<Lesson> actualLessons = lessonDao.getAllByAudienceIdDateAndLessonTimeId(1, LocalDate.parse("2017-06-01"), 1);

        assertEquals(singletonList(retrievedLesson), actualLessons);
    }

    @Test
    void givenNonExistentId_whenGetById_thenReturnedOptionalEmpty() {
        Optional<Lesson> actualLesson = lessonDao.getById(4);

        assertEquals(Optional.empty(), actualLesson);
    }

    @Test
    void givenEmptyTable_whenGetAll_thenReturnedEmptyList() {
        sessionFactory.getCurrentSession().createNativeQuery("DELETE FROM lessons").executeUpdate();

        List<Lesson> actualLessons = lessonDao.getAll();

        assertEquals(emptyList(), actualLessons);
    }

    @Test
    void givenNonExistentDate_whenGetAllByDate_thenReturnedEmptyList() {
        List<Lesson> actualLessons = lessonDao.getAllByDate(LocalDate.parse("2038-06-01"));

        assertEquals(emptyList(), actualLessons);
    }

    @Test
    void givenWrongData_whenGetAllByTeacherIdDateAndLessonTimeId_thenReturnedEmptyList() {
        List<Lesson> actualLessons = lessonDao.getAllByTeacherIdDateAndLessonTimeId(5, LocalDate.parse("2017-06-01"), 1);

        assertEquals(emptyList(), actualLessons);
    }

    @Test
    void givenWrongData_whenGetAllByAudienceIdDateAndLessonTimeId_thenReturnedEmptyList() {
        List<Lesson> actualLessons = lessonDao.getAllByAudienceIdDateAndLessonTimeId(5, LocalDate.parse("2017-06-01"), 1);

        assertEquals(emptyList(), actualLessons);
    }

    @Test
    void givenId1AndFirstDay_whenGetByDayForStudent_thenReturnedDayScheduleWithFirstDayAndFirstLesson() {
        List<Lesson> actualLessons = lessonDao.getByDateForStudent(1, LocalDate.parse("2017-06-01"));

        assertEquals(retrievedDaySchedule.getLessons(), actualLessons);
    }

    @Test
    void givenId1AndFirstDay_whenGetByDayForTeacher_thenReturnedDayScheduleWithFirstDayAndFirstLesson() {
        List<Lesson> actualLessons = lessonDao.getByDateForTeacher(1, LocalDate.parse("2017-06-01"));

        assertEquals(retrievedDaySchedule.getLessons(), actualLessons);
    }

    @Test
    void givenId1AndFirstDay_whenGetByMonthForStudent_thenReturnedListOfDaysSchedulesWithFirstDayAndFirstLesson() {
        List<Lesson> actualLessons = lessonDao.getByMonthForStudent(1, LocalDate.parse("2017-06-01"));

        assertEquals(retrievedDaySchedule.getLessons(), actualLessons);
    }

    @Test
    void givenId1AndFirstDay_whenGetByMonthForTeacher_thenReturnedListOfDaysSchedulesWithFirstDayAndFirstLesson() {
        List<Lesson> actualLessons = lessonDao.getByMonthForStudent(1, LocalDate.parse("2017-06-01"));

        assertEquals(retrievedDaySchedule.getLessons(), actualLessons);
    }

    @Test
    void givenWrongData_whenGetByDayForStudent_thenReturnedScheduleWithoutLessons() {
        List<Lesson> actualLessons = lessonDao.getByDateForStudent(5, LocalDate.parse("2017-06-01"));

        assertTrue(actualLessons.isEmpty());
    }

    @Test
    void givenWrongData_whenGetByDayForTeacher_thenReturnedOptionalEmpty() {
        List<Lesson> actualLessons = lessonDao.getByDateForTeacher(5, LocalDate.parse("2017-06-01"));

        assertTrue(actualLessons.isEmpty());
    }

    @Test
    void givenWrongData_whenGetByMonthForStudent_thenReturnedEmptyList() {
        List<Lesson> actualLessons = lessonDao.getByMonthForStudent(5, LocalDate.parse("2017-06-01"));

        assertEquals(emptyList(), actualLessons);
    }

    @Test
    void givenWrongData_whenGetByMonthForTeacher_thenReturnedEmptyList() {
        List<Lesson> actualLessons = lessonDao.getByMonthForStudent(5, LocalDate.parse("2017-06-01"));

        assertEquals(emptyList(), actualLessons);
    }
}