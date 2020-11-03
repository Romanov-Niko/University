package com.foxminded.university.dao;

import com.foxminded.university.domain.Lesson;

import java.time.LocalDate;
import java.util.List;

public interface LessonDao extends Dao<Lesson> {

    List<Lesson> getAllByDate(LocalDate date);

    List<Lesson> getAllByTeacherIdDateAndLessonTimeId(int id, LocalDate date, int lessonTimeId);

    List<Lesson> getAllByAudienceIdDateAndLessonTimeId(int id, LocalDate date, int lessonTimeId);

    List<Lesson> getByDateForStudent(int id, LocalDate date);

    List<Lesson> getByDateForTeacher(int id, LocalDate date);

    List<Lesson> getByMonthForStudent(int id, LocalDate startDate);

    List<Lesson> getByMonthForTeacher(int id, LocalDate startDate);
}
