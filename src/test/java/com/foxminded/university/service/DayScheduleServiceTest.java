package com.foxminded.university.service;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.domain.DaySchedule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.foxminded.university.TestData.retrievedDaySchedule;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class DayScheduleServiceTest {

    @Mock
    private LessonDao lessonDao;

    @InjectMocks
    private DayScheduleService dayScheduleService;

    @Test
    void givenId1AndFirstDay_whenGetByDayForStudent_thenCalledDayScheduleDaoGetByDateForStudentAndReturnedScheduleOfGivenDate() {
        given(lessonDao.getByDateForStudent(1, LocalDate.parse("2017-06-01"))).willReturn(retrievedDaySchedule.getLessons());

        DaySchedule actualDaySchedule = dayScheduleService.getByDateForStudent(1, LocalDate.parse("2017-06-01"));

        assertEquals(retrievedDaySchedule, actualDaySchedule);
    }

    @Test
    void givenId1AndFirstDay_whenGetByDayForTeacher_thenCalledDayScheduleDaoGetByDateForTeacherAndReturnedScheduleOfGivenDate() {
        given(lessonDao.getByDateForTeacher(1, LocalDate.parse("2017-06-01"))).willReturn(retrievedDaySchedule.getLessons());

        DaySchedule actualDaySchedule = dayScheduleService.getByDateForTeacher(1, LocalDate.parse("2017-06-01"));

        assertEquals(retrievedDaySchedule, actualDaySchedule);
    }

    @Test
    void givenId1AndFirstDay_whenGetByMonthForStudent_thenCalledDayScheduleDaoGetByMonthForStudentAndReturnedAllSchedulesOfGivenMonth() {
        given(lessonDao.getByMonthForStudent(1, LocalDate.parse("2017-06-01"))).willReturn(retrievedDaySchedule.getLessons());

        List<DaySchedule> actualDaySchedules = dayScheduleService.getByMonthForStudent(1, LocalDate.parse("2017-06-01"));

        verify(lessonDao, times(1)).getByMonthForStudent(1, LocalDate.parse("2017-06-01"));
        assertEquals(singletonList(retrievedDaySchedule), actualDaySchedules);
    }

    @Test
    void givenId1AndFirstDay_whenGetByMonthForTeacher_thenCalledDayScheduleDaoGetByMonthForTeacherAndReturnedAllSchedulesOfGivenMonth() {
        given(lessonDao.getByMonthForTeacher(1, LocalDate.parse("2017-06-01"))).willReturn(retrievedDaySchedule.getLessons());

        List<DaySchedule> actualDaySchedules = dayScheduleService.getByMonthForTeacher(1, LocalDate.parse("2017-06-01"));

        verify(lessonDao, times(1)).getByMonthForTeacher(1, LocalDate.parse("2017-06-01"));
        assertEquals(singletonList(retrievedDaySchedule), actualDaySchedules);
    }

    @Test
    void givenDataThatProducesEmptyReturn_whenGetByDayForStudent_thenCalledDayScheduleDaoGetByDateForStudentAndReturnedScheduleWithoutLessons() {
        given(lessonDao.getByDateForStudent(1, LocalDate.parse("2017-06-01"))).willReturn(emptyList());

        DaySchedule actualDaySchedule = dayScheduleService.getByDateForStudent(1, LocalDate.parse("2017-06-01"));

        verify(lessonDao, times(1)).getByDateForStudent(1, LocalDate.parse("2017-06-01"));
        assertEquals(new DaySchedule(LocalDate.parse("2017-06-01")), actualDaySchedule);
    }

    @Test
    void givenDataThatProducesEmptyReturn_whenGetByDayForTeacher_thenCalledDayScheduleDaoGetByDateForStudentAndReturnedScheduleWithoutLessons() {
        given(lessonDao.getByDateForTeacher(1, LocalDate.parse("2017-06-01"))).willReturn(emptyList());

        DaySchedule actualDaySchedule = dayScheduleService.getByDateForTeacher(1, LocalDate.parse("2017-06-01"));

        verify(lessonDao, times(1)).getByDateForTeacher(1, LocalDate.parse("2017-06-01"));
        assertEquals(new DaySchedule(LocalDate.parse("2017-06-01")), actualDaySchedule);
    }

    @Test
    void givenDataThatProducesEmptyReturn_whenGetByMonthForStudent_thenCalledDayScheduleDaoGetByMonthForStudentAndReturnedEmptyList() {
        given(lessonDao.getByMonthForStudent(1, LocalDate.parse("2017-06-01"))).willReturn(emptyList());

        List<DaySchedule> actualDaySchedules = dayScheduleService.getByMonthForStudent(1, LocalDate.parse("2017-06-01"));

        verify(lessonDao, times(1)).getByMonthForStudent(1, LocalDate.parse("2017-06-01"));
        assertEquals(emptyList(), actualDaySchedules);
    }

    @Test
    void givenDataThatProducesEmptyReturn_whenGetByMonthForTeacher_thenCalledDayScheduleDaoGetByMonthForStudentAndReturnedEmptyList() {
        given(lessonDao.getByMonthForTeacher(1, LocalDate.parse("2017-06-01"))).willReturn(emptyList());

        List<DaySchedule> actualDaySchedules = dayScheduleService.getByMonthForTeacher(1, LocalDate.parse("2017-06-01"));

        verify(lessonDao, times(1)).getByMonthForTeacher(1, LocalDate.parse("2017-06-01"));
        assertEquals(emptyList(), actualDaySchedules);
    }
}