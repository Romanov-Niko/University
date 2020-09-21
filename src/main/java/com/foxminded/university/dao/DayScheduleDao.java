package com.foxminded.university.dao;

import com.foxminded.university.domain.DaySchedule;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

public interface DayScheduleDao extends Dao<DaySchedule> {

    DaySchedule getByDayForStudent(int id, LocalDate day);

    DaySchedule getByDayForTeacher(int id, LocalDate day);

    List<DaySchedule> getByMonthForStudent(int id, LocalDate startDay);

    List<DaySchedule> getByMonthForTeacher(int id, LocalDate startDay);
}
