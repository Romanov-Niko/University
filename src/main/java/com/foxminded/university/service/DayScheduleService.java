package com.foxminded.university.service;

import com.foxminded.university.dao.DayScheduleDao;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.domain.DaySchedule;
import com.foxminded.university.domain.Lesson;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class DayScheduleService {

    private final DayScheduleDao dayScheduleDao;
    private final LessonDao lessonDao;

    public DayScheduleService(DayScheduleDao dayScheduleDao, LessonDao lessonDao) {
        this.dayScheduleDao = dayScheduleDao;
        this.lessonDao = lessonDao;
    }

    public List<DaySchedule> getAll() {
        return dayScheduleDao.getAll();
    }

    public void save(DaySchedule daySchedule) {
        if (!isWeekendDay(daySchedule.getDay())) {
            dayScheduleDao.save(daySchedule);
        }
    }

    public void update(DaySchedule daySchedule) {
        if (isDaySchedulePresent(daySchedule.getId()) && !isWeekendDay(daySchedule.getDay()) && areLessonsPresent(daySchedule.getLessons())) {
            dayScheduleDao.update(daySchedule);
        }
    }

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

    private boolean isDaySchedulePresent(int id) {
        return dayScheduleDao.getById(id).isPresent();
    }

    private boolean isWeekendDay(LocalDate day) {
        return day.getDayOfWeek() == DayOfWeek.SATURDAY || day.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private boolean areLessonsPresent(List<Lesson> lessons) {
        return lessonDao.getAll().containsAll(lessons);
    }
}
