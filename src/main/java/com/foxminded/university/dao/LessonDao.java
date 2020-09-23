package com.foxminded.university.dao;

import com.foxminded.university.domain.DaySchedule;
import com.foxminded.university.domain.Lesson;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

public interface LessonDao extends Dao<Lesson> {

    List<Lesson> getAllByDate(LocalDate date);

    List<Lesson> getAllByTeacherIdDateAndLessonTimeId(int id, LocalDate date, int lessonTimeId);

    List<Lesson> getAllByAudienceIdDateAndLessonTimeId(int id, LocalDate date, int lessonTimeId);
}
