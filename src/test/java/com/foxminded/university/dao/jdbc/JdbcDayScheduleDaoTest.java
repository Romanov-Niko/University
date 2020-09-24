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
import java.util.Optional;

import static com.foxminded.university.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.util.Collections.*;

@Transactional
@SpringJUnitConfig(ApplicationTestConfig.class)
class JdbcDayScheduleDaoTest {

    @Autowired
    private DayScheduleDao dayScheduleDao;

    @Test
    void givenId1AndFirstDay_whenGetByDayForStudent_thenReturnedDayScheduleWithFirstDayAndFirstLesson() {
        Optional<DaySchedule> actualDaySchedule = dayScheduleDao.getByDateForStudent(1, LocalDate.parse("2017-06-01"));

        assertEquals(retrievedDaySchedule, actualDaySchedule.get());
    }

    @Test
    void givenId1AndFirstDay_whenGetByDayForTeacher_thenReturnedDayScheduleWithFirstDayAndFirstLesson() {
        Optional<DaySchedule> actualDaySchedule = dayScheduleDao.getByDateForTeacher(1, LocalDate.parse("2017-06-01"));

        assertEquals(retrievedDaySchedule, actualDaySchedule.get());
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

    @Test
    void givenWrongData_whenGetByDayForStudent_thenReturnedOptionalEmpty() {
        Optional<DaySchedule> actualDaySchedule = dayScheduleDao.getByDateForStudent(5, LocalDate.parse("2017-06-01"));

        assertEquals(Optional.empty(), actualDaySchedule);
    }

    @Test
    void givenWrongData_whenGetByDayForTeacher_thenReturnedOptionalEmpty() {
        Optional<DaySchedule> actualDaySchedule = dayScheduleDao.getByDateForTeacher(5, LocalDate.parse("2017-06-01"));

        assertEquals(Optional.empty(), actualDaySchedule);
    }

    @Test
    void givenWrongData_whenGetByMonthForStudent_thenReturnedEmptyList() {
        List<DaySchedule> actualDaySchedule = dayScheduleDao.getByMonthForStudent(5, LocalDate.parse("2017-06-01"));

        assertEquals(emptyList(), actualDaySchedule);
    }

    @Test
    void givenWrongData_whenGetByMonthForTeacher_thenReturnedEmptyList() {
        List<DaySchedule> actualDaySchedule = dayScheduleDao.getByMonthForStudent(5, LocalDate.parse("2017-06-01"));

        assertEquals(emptyList(), actualDaySchedule);
    }
}

