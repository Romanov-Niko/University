package com.foxminded.university.service;

import com.foxminded.university.dao.DayScheduleDao;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.domain.DaySchedule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static com.foxminded.university.TestData.*;


@ExtendWith(MockitoExtension.class)
class DayScheduleServiceTest {

    @Mock
    private DayScheduleDao dayScheduleDao;

    @InjectMocks
    private DayScheduleService dayScheduleService;


    @Test
    void givenId1AndFirstDay_whenGetByDayForStudent_thenCalledDayScheduleDaoGetByDateForStudentAndReturnedScheduleOfGivenDate() {
        given(dayScheduleService.getByDateForStudent(1, LocalDate.parse("2017-06-01"))).willReturn(retrievedDaySchedule);

        DaySchedule actualDaySchedule = dayScheduleService.getByDateForStudent(1, LocalDate.parse("2017-06-01"));

        verify(dayScheduleDao, times(1)).getByDateForStudent(1, LocalDate.parse("2017-06-01"));
        assertEquals(retrievedDaySchedule, actualDaySchedule);
    }

    @Test
    void givenId1AndFirstDay_whenGetByDayForTeacher_thenCalledDayScheduleDaoGetByDateForTeacherAndReturnedScheduleOfGivenDate() {
        given(dayScheduleService.getByDateForTeacher(1, LocalDate.parse("2017-06-01"))).willReturn(retrievedDaySchedule);

        DaySchedule actualDaySchedule = dayScheduleService.getByDateForTeacher(1, LocalDate.parse("2017-06-01"));

        verify(dayScheduleDao, times(1)).getByDateForTeacher(1, LocalDate.parse("2017-06-01"));
        assertEquals(retrievedDaySchedule, actualDaySchedule);
    }

    @Test
    void givenId1AndFirstDay_whenGetByMonthForStudent_thenCalledDayScheduleDaoGetByMonthForStudentAndReturnedAllSchedulesOfGivenMonth() {
        given(dayScheduleService.getByMonthForStudent(1, LocalDate.parse("2017-06-01"))).willReturn(singletonList(retrievedDaySchedule));

        List<DaySchedule> actualDaySchedules = dayScheduleService.getByMonthForStudent(1, LocalDate.parse("2017-06-01"));

        verify(dayScheduleDao, times(1)).getByMonthForStudent(1, LocalDate.parse("2017-06-01"));
        assertEquals(singletonList(retrievedDaySchedule), actualDaySchedules);
    }

    @Test
    void givenId1AndFirstDay_whenGetByMonthForTeacher_thenCalledDayScheduleDaoGetByMonthForTeacherAndReturnedAllSchedulesOfGivenMonth() {
        given(dayScheduleService.getByMonthForTeacher(1, LocalDate.parse("2017-06-01"))).willReturn(singletonList(retrievedDaySchedule));

        List<DaySchedule> actualDaySchedules = dayScheduleService.getByMonthForTeacher(1, LocalDate.parse("2017-06-01"));

        verify(dayScheduleDao, times(1)).getByMonthForTeacher(1, LocalDate.parse("2017-06-01"));
        assertEquals(singletonList(retrievedDaySchedule), actualDaySchedules);
    }

}