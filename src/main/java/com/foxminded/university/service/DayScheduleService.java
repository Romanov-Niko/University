package com.foxminded.university.service;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.domain.DaySchedule;
import com.foxminded.university.domain.Lesson;
import javafx.beans.property.ObjectProperty;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class DayScheduleService {

    private final LessonDao lessonDao;

    public DayScheduleService(LessonDao lessonDao) {
        this.lessonDao = lessonDao;
    }

    public DaySchedule getByDateForStudent(int id, LocalDate day) {
        return new DaySchedule(day, lessonDao.getByDateForStudent(id, day));
    }

    public DaySchedule getByDateForTeacher(int id, LocalDate day) {
        return new DaySchedule(day, lessonDao.getByDateForTeacher(id, day));
    }

    public List<DaySchedule> getByMonthForStudent(int id, LocalDate startDay) {
        return getSchedules(lessonDao.getByMonthForStudent(id, startDay));
    }

    public List<DaySchedule> getByMonthForTeacher(int id, LocalDate startDay) {
        return getSchedules(lessonDao.getByMonthForTeacher(id, startDay));
    }

    private List<DaySchedule> getSchedules(List<Lesson> lessons) {
        List<DaySchedule> daySchedules = new ArrayList<>();
        List<LocalDate> usedDates = new ArrayList<>();
        lessons.forEach(lesson -> {
            LocalDate date = lesson.getDate();
            if (!usedDates.contains(lesson.getDate())) {
                List<Lesson> lessonsByDate = lessons.stream().filter(unfilteredLesson -> unfilteredLesson.getDate().equals(date)).collect(toList());
                daySchedules.add(new DaySchedule(date, lessonsByDate));
                usedDates.add(lesson.getDate());
            }
        });
        return daySchedules;
    }
}
