package com.foxminded.university.dao.jdbc;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.domain.LessonTime;
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

@Transactional
@SpringJUnitConfig(ApplicationTestConfig.class)
class JdbcLessonTimeDaoTest {

    @Autowired
    private LessonTimeDao lessonTimeDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenId1_whenGetById_thenReturnedFirstLessonTime() {
        LessonTime actualLessonTime = lessonTimeDao.getById(1).orElse(null);

        assertEquals(retrievedLessonTime, actualLessonTime);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedListOfAllLessonsTimes() {
        int expectedRows = lessonTimeDao.getAll().size();

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_times");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenLessonTime_whenSave_thenAddedGivenLessonTime() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_times") + 1;

        lessonTimeDao.save(createdLessonTime);

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_times");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenLessonTime_whenUpdate_thenUpdatedLessonTimeWithEqualId() {
        lessonTimeDao.update(updatedLessonTime);

        int actualNumber = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons_times", String.format(
                "id = %d AND begin_time = '%s' AND end_time = '%s'", updatedLessonTime.getId(),
                updatedLessonTime.getBegin(), updatedLessonTime.getEnd()));
        assertEquals(1, actualNumber);
    }

    @Test
    void givenId3_whenDelete_thenDeletedThirdLessonTime() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_times") - 1;

        lessonTimeDao.delete(3);

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_times");
        assertEquals(expectedRows, actualRows);
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
    void givenNonExistentTable_whenGetAll_thenReturnedEmptyList() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "lessons_times");

        List<LessonTime> actualLessonsTimes = lessonTimeDao.getAll();

        assertEquals(emptyList(), actualLessonsTimes);
    }

    @Test
    void givenWrongTime_whenGetByStartAndEndTime_thenReturnedOptionalEmpty() {
        Optional<LessonTime> actualLessonTime = lessonTimeDao.getByStartAndEndTime(LocalTime.parse("01:00:00"), LocalTime.parse("02:00:00"));

        assertEquals(Optional.empty(), actualLessonTime);
    }
}