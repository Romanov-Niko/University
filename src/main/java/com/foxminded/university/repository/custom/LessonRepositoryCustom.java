package com.foxminded.university.repository.custom;

import com.foxminded.university.domain.Lesson;

import java.time.LocalDate;
import java.util.List;

public interface LessonRepositoryCustom {

    List<Lesson> findAllByTeacherIdDateAndLessonTimeId(int id, LocalDate date, int lessonTimeId);

    List<Lesson> findAllByAudienceIdDateAndLessonTimeId(int id, LocalDate date, int lessonTimeId);

    List<Lesson> findByDateForStudent(int id, LocalDate date);

    List<Lesson> findByDateForTeacher(int id, LocalDate date);

    List<Lesson> findByMonthForStudent(int id, LocalDate startDate);

    List<Lesson> findByMonthForTeacher(int id, LocalDate startDate);
}
