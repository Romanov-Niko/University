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
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.foxminded.university.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.util.Collections.*;

@Transactional
@SpringJUnitConfig(ApplicationTestConfig.class)
class JdbcDayScheduleDaoTest {

    @Autowired
    private DayScheduleDao dayScheduleDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenId1_whenGetById_thenReturnedFirstDaySchedule() {
        DaySchedule actualDaySchedule = dayScheduleDao.getById(1).orElse(null);

        assertEquals(retrievedDaySchedule, actualDaySchedule);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedListOfAllDaysSchedules() {
        int expectedRows = dayScheduleDao.getAll().size();

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "days_schedules");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenDaySchedule_whenSave_thenAddedGivenDaySchedule() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "days_schedules") + 1;

        dayScheduleDao.save(createdDaySchedule);

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "days_schedules");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenDaySchedule_whenUpdate_thenUpdatedDayscheduleWithEqualId() {
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

    @Test
    void givenId1AndFirstDay_whenGetByDayForStudent_thenReturnedDayScheduleWithFirstDayAndFirstLesson() {
        DaySchedule actualDaySchedule = dayScheduleDao.getByDayForStudent(1, LocalDate.parse("2017-06-01"));

        assertEquals(retrievedDaySchedule, actualDaySchedule);
    }

    @Test
    void givenId1AndFirstDay_whenGetByDayForTeacher_thenReturnedDayScheduleWithFirstDayAndFirstLesson() {
        DaySchedule actualDaySchedule = dayScheduleDao.getByDayForTeacher(1, LocalDate.parse("2017-06-01"));

        assertEquals(retrievedDaySchedule, actualDaySchedule);
    }

    @Test
    void givenId1AndFirstDay_whenGetByMonthForStudent_thenReturnedListOfDaysSchedulesWithFirstDayAndFirstLesson() {
        List<DaySchedule> actualDaysSchedules = dayScheduleDao.getByMonthForStudent(1, LocalDate.parse("2017-06-01"));

        assertEquals(singletonList(retrievedDaySchedule), actualDaysSchedules);
    }

    @Test
    void givenId1AndFirstDay_whenGetByMonthForTeacher_thenReturnedListOfDaysSchedulesWithFirstDayAndFirstLesson() {
        List<DaySchedule> actualDaysSchedules = dayScheduleDao.getByMonthForStudent(1, LocalDate.parse("2017-06-01"));

        assertEquals(singletonList(retrievedDaySchedule), actualDaysSchedules);
    }
}