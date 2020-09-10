package com.foxminded.university.dao.jdbc;

import com.foxminded.university.EntitiesForTests;
import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.util.Collections.singletonList;

import static org.junit.jupiter.api.Assertions.*;

@Sql({"/schema.sql", "/data.sql"})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationTestConfig.class})
class JdbcLessonDaoTest {

    @Autowired
    private LessonDao lessonDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void getById() {
        Lesson expectedLesson = EntitiesForTests.lessonGetById;
        Lesson actualLesson = lessonDao.getById(1);
        assertEquals(expectedLesson, actualLesson);
    }

    @Test
    void getAll() {
        int expectedRows = lessonDao.getAll().size();
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void save() {
        Lesson expectedLesson = EntitiesForTests.lessonSave;
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons") + 1;
        lessonDao.save(expectedLesson);
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void update() {
        Lesson lessonForUpdate = EntitiesForTests.lessonUpdate;
        lessonDao.update(lessonForUpdate);
        int updatedLesson = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons", String.format(
                "lesson_id = %d AND subject_id = %d AND teacher_id = %d AND audience_id = %d AND lesson_time_id = %d",
                lessonForUpdate.getId(), lessonForUpdate.getSubject().getId(), lessonForUpdate.getTeacher().getId(),
                lessonForUpdate.getAudience().getId(), lessonForUpdate.getLessonTime().getId()));
        assertEquals(1, updatedLesson);
    }

    @Test
    void delete() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons") - 1;
        lessonDao.delete(3);
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void getAllByDay() {
        List<Lesson> expectedLessons = singletonList(EntitiesForTests.lessonGetById);
        List<Lesson> actualLessons = lessonDao.getAllByDayId(1);
        assertEquals(expectedLessons, actualLessons);
    }
}