package com.foxminded.university.dao.jdbc;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.DayScheduleDao;
import com.foxminded.university.domain.DaySchedule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import static com.foxminded.university.EntitiesForTests.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql({"/schema.sql", "/data.sql"})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationTestConfig.class})
class JdbcDayScheduleDaoTest {

    @Autowired
    private DayScheduleDao dayScheduleDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenId1_whenGetById_thenReturnedFirstDaySchedule() {
        DaySchedule actualDaySchedule = dayScheduleDao.getById(1);

        assertEquals(retrievedDaySchedule, actualDaySchedule);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedListOfAllDaysSchedules() {
        int expectedRows = dayScheduleDao.getAll().size();

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "days_schedules");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenDayschedule_whenSave_thenAddedGivenDaySchedule() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "days_schedules") + 1;

        dayScheduleDao.save(createdDaySchedule);

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "days_schedules");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenDayschedule_whenUpdate_thenUpdatedDayscheduleWithEqualId() {
        dayScheduleDao.update(updatedDaySchedule);

        int actualNumber = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "days_schedules", String.format(
                "id = %d AND day = '%s'", updatedDaySchedule.getId(), updatedDaySchedule.getDay()
        ));
        assertEquals(1, actualNumber);
    }

    @Test
    void givenId3_whenDelete_thenDeletedThirdDaySchedule() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "days_schedules") - 1;

        dayScheduleDao.delete(3);

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "days_schedules");
        assertEquals(expectedRows, actualRows);
    }
}