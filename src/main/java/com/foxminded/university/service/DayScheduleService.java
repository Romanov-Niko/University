package com.foxminded.university.service;

import com.foxminded.university.dao.DayScheduleDao;
import com.foxminded.university.domain.DaySchedule;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DayScheduleService {

    private final DayScheduleDao dayScheduleDao;

    public DayScheduleService(DayScheduleDao dayScheduleDao) {
        this.dayScheduleDao = dayScheduleDao;
    }

    public Optional<DaySchedule> getByDateForStudent(int id, LocalDate day) {
        return dayScheduleDao.getByDateForStudent(id, day);
    }

    public Optional<DaySchedule> getByDateForTeacher(int id, LocalDate day) {
        return dayScheduleDao.getByDateForTeacher(id, day);
    }

    public List<DaySchedule> getByMonthForStudent(int id, LocalDate startDay) {
        return dayScheduleDao.getByMonthForStudent(id, startDay);
    }

    public List<DaySchedule> getByMonthForTeacher(int id, LocalDate startDay) {
        return dayScheduleDao.getByMonthForTeacher(id, startDay);
    }


}
