package com.foxminded.university.dao.jdbc;

import com.foxminded.university.EntitiesForTests;
import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Lesson;
import com.foxminded.university.domain.LessonTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@Sql({"/schema.sql", "/data.sql"})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationTestConfig.class})
class JdbcLessonTimeDaoTest {

    @Autowired
    private LessonTimeDao lessonTimeDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void getById() {
        LessonTime expectedLessonTime = EntitiesForTests.lessonTimeGetById;
        LessonTime actualLessonTime = lessonTimeDao.getById(1);
        assertEquals(expectedLessonTime, actualLessonTime);
    }

    @Test
    void getAll() {
        int expectedRows = lessonTimeDao.getAll().size();
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_times");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void save() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_times") + 1;
        lessonTimeDao.save(EntitiesForTests.lessonTimeSave);
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_times");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void update() {
        LessonTime lessonTimeForUpdate = EntitiesForTests.lessonTimeUpdate;
        lessonTimeDao.update(lessonTimeForUpdate);
        int updatedLessonTime = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons_times", String.format(
                "lesson_time_id = %d AND begin_time = '%s' AND end_time = '%s'", lessonTimeForUpdate.getId(),
                lessonTimeForUpdate.getBegin(), lessonTimeForUpdate.getEnd()));
        assertEquals(1, updatedLessonTime);
    }

    @Test
    void delete() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_times") - 1;
        lessonTimeDao.delete(3);
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_times");
        assertEquals(expectedRows, actualRows);
    }
}