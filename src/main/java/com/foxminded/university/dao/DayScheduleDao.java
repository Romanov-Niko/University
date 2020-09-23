package com.foxminded.university.dao;

import com.foxminded.university.domain.DaySchedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DayScheduleDao {

    Optional<DaySchedule> getByDateForStudent(int id, LocalDate date);

    Optional<DaySchedule> getByDateForTeacher(int id, LocalDate date);

    List<DaySchedule> getByMonthForStudent(int id, LocalDate startDate);

    List<DaySchedule> getByMonthForTeacher(int id, LocalDate startDate);
}

