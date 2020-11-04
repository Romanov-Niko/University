package com.foxminded.university.repository;

import com.foxminded.university.domain.Lesson;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, Integer> {

    List<Lesson> findAll();

    List<Lesson> findAllByDate(LocalDate date);

    List<Lesson> findAllByTeacherIdAndDateAndLessonTimeId(int id, LocalDate date, int lessonTimeId);

    List<Lesson> findAllByAudienceIdAndDateAndLessonTimeId(int id, LocalDate date, int lessonTimeId);

    @Query(value = "SELECT * FROM lessons " +
            "LEFT JOIN lessons_groups ON lessons.id = lessons_groups.lesson_id " +
            "LEFT JOIN students ON lessons_groups.group_id = students.group_id " +
            "WHERE students.id = :studentId AND lessons.date = :day", nativeQuery = true)
    List<Lesson> findByDateForStudent(@Param("studentId") int id, @Param("day") LocalDate date);

    @Query(value = "SELECT * FROM lessons " +
            "WHERE lessons.teacher_id = :teacherId AND lessons.date = :day", nativeQuery = true)
    List<Lesson> findByDateForTeacher(@Param("teacherId") int id, @Param("day") LocalDate date);

    @Query(value = "SELECT * FROM lessons " +
            "LEFT JOIN lessons_groups ON lessons.id = lessons_groups.lesson_id " +
            "LEFT JOIN students ON lessons_groups.group_id = students.group_id " +
            "WHERE students.id = :studentId AND lessons.date >= :startDay AND lessons.date < :endDay", nativeQuery = true)
    List<Lesson> findByMonthForStudent(@Param("studentId") int id, @Param("startDay") LocalDate startDate, @Param("endDay") LocalDate endDate);

    @Query(value = "SELECT * FROM lessons " +
            "WHERE lessons.teacher_id = :teacherId AND lessons.date >= :startDay AND lessons.date < :endDay", nativeQuery = true)
    List<Lesson> findByMonthForTeacher(@Param("teacherId") int id, @Param("startDay") LocalDate startDate, @Param("endDay") LocalDate endDate);
}
