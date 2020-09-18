package com.foxminded.university.service;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.dao.DayScheduleDao;
import com.foxminded.university.domain.DaySchedule;
import com.foxminded.university.domain.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DayScheduleService implements Service<DaySchedule> {

    private final DayScheduleDao dayScheduleDao;

    @Autowired
    public DayScheduleService(DayScheduleDao dayScheduleDao) {
        this.dayScheduleDao = dayScheduleDao;
    }

    @Override
    public DaySchedule getById(int id) {
        return dayScheduleDao.getById(id);
    }

    @Override
    public List<DaySchedule> getAll() {
        return dayScheduleDao.getAll();
    }

    @Override
    public void save(DaySchedule daySchedule) {
        dayScheduleDao.save(daySchedule);
    }

    @Override
    public void update(DaySchedule daySchedule) {
        dayScheduleDao.update(daySchedule);
    }

    @Override
    public void delete(int id) {
        dayScheduleDao.delete(id);
    }

    public DaySchedule getByDayForStudent(int id, LocalDate day) {
        return dayScheduleDao.getByDayForStudent(id, day);
    }

    public DaySchedule getByDayForTeacher(int id, LocalDate day) {
        return dayScheduleDao.getByDayForTeacher(id, day);
    }

    public List<DaySchedule> getByMonthForStudent(int id, LocalDate startDay) {
        return dayScheduleDao.getByMonthForStudent(id, startDay);
    }

    public List<DaySchedule> getByMonthForTeacher(int id, LocalDate startDay) {
        return dayScheduleDao.getByMonthForTeacher(id, startDay);
    }
}
