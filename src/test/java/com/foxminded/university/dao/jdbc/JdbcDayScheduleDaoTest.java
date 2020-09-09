package com.foxminded.university.dao.jdbc;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.dao.DayScheduleDao;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.DaySchedule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;

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
        DaySchedule expectedDaySchedule = new DaySchedule(1, LocalDate.parse("2017-06-01"), new ArrayList<>());
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
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}