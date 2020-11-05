package com.foxminded.university.service;

import com.foxminded.university.domain.DaySchedule;
import com.foxminded.university.repository.LessonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static com.foxminded.university.TestData.retrievedDaySchedule;
import static com.foxminded.university.TestData.retrievedTeacher;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class DayScheduleServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private DayScheduleService dayScheduleService;

    @Test
    void givenId1AndFirstDay_whenGetByDayForStudent_thenCalledDayScheduleDaoGetByDateForStudentAndReturnedScheduleOfGivenDate() {
        given(lessonRepository.findByDateForStudent(1, LocalDate.parse("2017-06-01"))).willReturn(retrievedDaySchedule.getLessons());

        DaySchedule actualDaySchedule = dayScheduleService.findByDateForStudent(1, LocalDate.parse("2017-06-01"));

        assertEquals(retrievedDaySchedule, actualDaySchedule);
    }

    @Test
    void givenId1AndFirstDay_whenGetByDayForTeacher_thenCalledDayScheduleDaoGetByDateForTeacherAndReturnedScheduleOfGivenDate() {
        given(lessonRepository.findAllByTeacherAndDate(retrievedTeacher, LocalDate.parse("2017-06-01"))).willReturn(retrievedDaySchedule.getLessons());

        DaySchedule actualDaySchedule = dayScheduleService.findByDateForTeacher(retrievedTeacher, LocalDate.parse("2017-06-01"));

        assertEquals(retrievedDaySchedule, actualDaySchedule);
    }

    @Test
    void givenId1AndFirstDay_whenGetByMonthForStudent_thenCalledDayScheduleDaoGetByMonthForStudentAndReturnedAllSchedulesOfGivenMonth() {
        given(lessonRepository.findByMonthForStudent(1, LocalDate.parse("2017-06-01"), LocalDate.parse("2017-06-01").plusMonths(1))).willReturn(retrievedDaySchedule.getLessons());

        List<DaySchedule> actualDaySchedules = dayScheduleService.findByMonthForStudent(1, LocalDate.parse("2017-06-01"));

        verify(lessonRepository, times(1)).findByMonthForStudent(1, LocalDate.parse("2017-06-01"), LocalDate.parse("2017-06-01").plusMonths(1));
        assertEquals(singletonList(retrievedDaySchedule), actualDaySchedules);
    }

    @Test
    void givenId1AndFirstDay_whenGetByMonthForTeacher_thenCalledDayScheduleDaoGetByMonthForTeacherAndReturnedAllSchedulesOfGivenMonth() {
        given(lessonRepository.findAllByTeacherAndDateGreaterThanEqualAndDateLessThan(retrievedTeacher, LocalDate.parse("2017-06-01"), LocalDate.parse("2017-06-01").plusMonths(1))).willReturn(retrievedDaySchedule.getLessons());

        List<DaySchedule> actualDaySchedules = dayScheduleService.findByMonthForTeacher(retrievedTeacher, LocalDate.parse("2017-06-01"));

        verify(lessonRepository, times(1)).findAllByTeacherAndDateGreaterThanEqualAndDateLessThan(retrievedTeacher, LocalDate.parse("2017-06-01"), LocalDate.parse("2017-06-01").plusMonths(1));
        assertEquals(singletonList(retrievedDaySchedule), actualDaySchedules);
    }

    @Test
    void givenDataThatProducesEmptyReturn_whenGetByDayForStudent_thenCalledDayScheduleDaoGetByDateForStudentAndReturnedScheduleWithoutLessons() {
        given(lessonRepository.findByDateForStudent(1, LocalDate.parse("2017-06-01"))).willReturn(emptyList());

        DaySchedule actualDaySchedule = dayScheduleService.findByDateForStudent(1, LocalDate.parse("2017-06-01"));

        verify(lessonRepository, times(1)).findByDateForStudent(1, LocalDate.parse("2017-06-01"));
        assertEquals(new DaySchedule(LocalDate.parse("2017-06-01")), actualDaySchedule);
    }

    @Test
    void givenDataThatProducesEmptyReturn_whenGetByDayForTeacher_thenCalledDayScheduleDaoGetByDateForStudentAndReturnedScheduleWithoutLessons() {
        given(lessonRepository.findAllByTeacherAndDate(retrievedTeacher, LocalDate.parse("2017-06-01"))).willReturn(emptyList());

        DaySchedule actualDaySchedule = dayScheduleService.findByDateForTeacher(retrievedTeacher, LocalDate.parse("2017-06-01"));

        verify(lessonRepository, times(1)).findAllByTeacherAndDate(retrievedTeacher, LocalDate.parse("2017-06-01"));
        assertEquals(new DaySchedule(LocalDate.parse("2017-06-01")), actualDaySchedule);
    }

    @Test
    void givenDataThatProducesEmptyReturn_whenGetByMonthForStudent_thenCalledDayScheduleDaoGetByMonthForStudentAndReturnedEmptyList() {
        given(lessonRepository.findByMonthForStudent(1, LocalDate.parse("2017-06-01"), LocalDate.parse("2017-06-01").plusMonths(1))).willReturn(emptyList());

        List<DaySchedule> actualDaySchedules = dayScheduleService.findByMonthForStudent(1, LocalDate.parse("2017-06-01"));

        verify(lessonRepository, times(1)).findByMonthForStudent(1, LocalDate.parse("2017-06-01"), LocalDate.parse("2017-06-01").plusMonths(1));
        assertEquals(emptyList(), actualDaySchedules);
    }

    @Test
    void givenDataThatProducesEmptyReturn_whenGetByMonthForTeacher_thenCalledDayScheduleDaoGetByMonthForStudentAndReturnedEmptyList() {
        given(lessonRepository.findAllByTeacherAndDateGreaterThanEqualAndDateLessThan(retrievedTeacher, LocalDate.parse("2017-06-01"), LocalDate.parse("2017-06-01").plusMonths(1))).willReturn(emptyList());

        List<DaySchedule> actualDaySchedules = dayScheduleService.findByMonthForTeacher(retrievedTeacher, LocalDate.parse("2017-06-01"));

        verify(lessonRepository, times(1)).findAllByTeacherAndDateGreaterThanEqualAndDateLessThan(retrievedTeacher, LocalDate.parse("2017-06-01"), LocalDate.parse("2017-06-01").plusMonths(1));
        assertEquals(emptyList(), actualDaySchedules);
    }
}