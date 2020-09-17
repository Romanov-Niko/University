package com.foxminded.university.dao.jdbc;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.domain.Lesson;
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
class JdbcLessonDaoTest {

    @Autowired
    private LessonDao lessonDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenId1_whenGetById_thenReturnedFirstGroup() {
        Lesson actualLesson = lessonDao.getById(1);

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
                "id = %d AND subject_id = %d AND teacher_id = %d AND audience_id = %d AND lesson_time_id = %d",
                updatedLesson.getId(), updatedLesson.getSubject().getId(), updatedLesson.getTeacher().getId(),
                updatedLesson.getAudience().getId(), updatedLesson.getLessonTime().getId()));
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
    void givenId1_whenGetAllByDayId_thenReturnedAllLessonsOfFirstDay() {
        List<Lesson> expectedLessons = singletonList(retrievedLesson);

        List<Lesson> actualLessons = lessonDao.getAllByDayId(1);

        assertEquals(expectedLessons, actualLessons);
    }
}