package com.foxminded.university.service;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.dao.DayScheduleDao;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.domain.DaySchedule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static com.foxminded.university.TestData.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DayScheduleServiceTest {

    @Mock
    private DayScheduleDao dayScheduleDao;

    @Mock
    private LessonDao lessonDao;

    @InjectMocks
    private DayScheduleService dayScheduleService;

    @Test
    void getAll() {
        dayScheduleService.getAll();

        verify(dayScheduleDao, times(1)).getAll();
    }

    @Test
    void save() {
        dayScheduleService.save(createdDaySchedule);

        verify(dayScheduleDao, times(1)).save(createdDaySchedule);
    }

    @Test
    void update() {
        given(dayScheduleDao.getById(anyInt())).willReturn(Optional.of(retrievedDaySchedule));
        given(lessonDao.getAll()).willReturn(singletonList(retrievedLesson));

        dayScheduleService.update(updatedDaySchedule);

        verify(dayScheduleDao, times(1)).update(updatedDaySchedule);
    }

    @Test
    void delete() {
        dayScheduleService.delete(1);

        verify(dayScheduleDao, times(1)).delete(1);
    }

    @Test
    void getByDayForStudent() {
        dayScheduleService.getByDayForStudent(1, LocalDate.parse("2017-06-01"));

        verify(dayScheduleDao, times(1)).getByDayForStudent(1, LocalDate.parse("2017-06-01"));
    }

    @Test
    void getByDayForTeacher() {
        dayScheduleService.getByDayForTeacher(1, LocalDate.parse("2017-06-01"));

        verify(dayScheduleDao, times(1)).getByDayForTeacher(1, LocalDate.parse("2017-06-01"));
    }

    @Test
    void getByMonthForStudent() {
        dayScheduleService.getByMonthForStudent(1, LocalDate.parse("2017-06-01"));

        verify(dayScheduleDao, times(1)).getByMonthForStudent(1, LocalDate.parse("2017-06-01"));
    }

    @Test
    void getByMonthForTeacher() {
        dayScheduleService.getByMonthForTeacher(1, LocalDate.parse("2017-06-01"));

        verify(dayScheduleDao, times(1)).getByMonthForTeacher(1, LocalDate.parse("2017-06-01"));
    }
}