package com.foxminded.university.service;

import com.foxminded.university.domain.DaySchedule;
import com.foxminded.university.domain.Lesson;
import com.foxminded.university.domain.Teacher;
import com.foxminded.university.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class DayScheduleService {

    private final LessonRepository lessonRepository;

    public DayScheduleService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public DaySchedule findByDateForStudent(int id, LocalDate day) {
        return new DaySchedule(day, lessonRepository.findByDateForStudent(id, day));
    }

    public DaySchedule findByDateForTeacher(Teacher teacher, LocalDate day) {
        return new DaySchedule(day, lessonRepository.findAllByTeacherAndDate(teacher, day));
    }

    public List<DaySchedule> findByMonthForStudent(int id, LocalDate startDay) {
        return getSchedules(lessonRepository.findByMonthForStudent(id, startDay, startDay.plusMonths(1)));
    }

    public List<DaySchedule> findByMonthForTeacher(Teacher teacher, LocalDate startDay) {
        return getSchedules(lessonRepository.findAllByTeacherAndDateGreaterThanEqualAndDateLessThan(teacher, startDay, startDay.plusMonths(1)));
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
