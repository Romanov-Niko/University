package com.foxminded.university.dao.jdbc;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.domain.Lesson;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringJUnitConfig(ApplicationTestConfig.class)
class JdbcLessonDaoTest {

    @Autowired
    private LessonDao lessonDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenId1_whenGetById_thenReturnedFirstGroup() {
        Lesson actualLesson = lessonDao.getById(1).orElse(null);

        assertEquals(retrievedLesson, actualLesson);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedListOfAllLessons() {
        int expectedRows = lessonDao.getAll().size();

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenLesson_whenSave_thenAddedGivenLesson() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons") + 1;

        lessonDao.save(createdLesson);

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenLesson_whenUpdate_thenUpdatedLessonWithEqualId() {
        lessonDao.update(updatedLesson);

        int actualNumber = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons", String.format(
                "id = %d AND subject_id = %d AND teacher_id = %d AND audience_id = %d AND lesson_time_id = %d AND date = '%s'",
                updatedLesson.getId(), updatedLesson.getSubject().getId(), updatedLesson.getTeacher().getId(),
                updatedLesson.getAudience().getId(), updatedLesson.getLessonTime().getId(), updatedLesson.getDate()));
        assertEquals(1, actualNumber);
    }

    @Test
    void givenId3_whenDelete_thenDeletedThirdLesson() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons") - 1;

        lessonDao.delete(3);

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons");
        assertEquals(expectedRows, actualRows);
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
    void givenNonExistentTable_whenGetAll_thenReturnedEmptyList() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "lessons");

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
}