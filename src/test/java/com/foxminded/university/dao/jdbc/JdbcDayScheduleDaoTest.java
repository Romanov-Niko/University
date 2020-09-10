package com.foxminded.university.dao.jdbc;

import com.foxminded.university.EntitiesForTests;
import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.dao.DayScheduleDao;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Sql({"/schema.sql", "/data.sql"})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationTestConfig.class})
class JdbcDayScheduleDaoTest {

    @Autowired
    private DayScheduleDao dayScheduleDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void getById() {
        DaySchedule expectedDaySchedule = EntitiesForTests.dayScheduleGetById;
        DaySchedule actualDaySchedule = dayScheduleDao.getById(1);
        assertEquals(expectedDaySchedule, actualDaySchedule);
    }

    @Test
    void getAll() {
        int expectedRows = dayScheduleDao.getAll().size();
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "days");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void save() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "days") + 1;
        dayScheduleDao.save(EntitiesForTests.dayScheduleSave);
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "days");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void update() {
        DaySchedule dayScheduleForUpdate = EntitiesForTests.dayScheduleUpdate;
        dayScheduleDao.update(dayScheduleForUpdate);
        int updatedAudience = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "days", String.format(
                "day_id = %d AND day = '%s'", dayScheduleForUpdate.getId(), dayScheduleForUpdate.getDay()
        ));
        assertEquals(1, updatedAudience);
    }

    @Test
    void delete() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "days") - 1;
        dayScheduleDao.delete(3);
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "days");
        assertEquals(expectedRows, actualRows);
    }
}