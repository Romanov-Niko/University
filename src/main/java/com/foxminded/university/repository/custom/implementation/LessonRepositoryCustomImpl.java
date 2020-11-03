package com.foxminded.university.repository.custom.implementation;

import com.foxminded.university.domain.Lesson;
import com.foxminded.university.repository.custom.LessonRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Repository
public class LessonRepositoryCustomImpl implements LessonRepositoryCustom {

    private static final Logger logger = LoggerFactory.getLogger(LessonRepositoryCustomImpl.class);

    private static final String SQL_GET_ALL_BY_TEACHER_ID_DATE_AND_TIME_ID = "SELECT * FROM lessons " +
            "WHERE teacher_id = :teacherId AND date = :date AND lesson_time_id = :lessonTimeId";
    private static final String SQL_GET_ALL_BY_AUDIENCE_ID_DATE_AND_TIME_ID = "SELECT * FROM lessons " +
            "WHERE audience_id = :audienceId AND date = :date AND lesson_time_id = :lessonTimeId";
    private static final String SQL_GET_LESSONS_BY_DAY_FOR_STUDENT = "SELECT * FROM lessons " +
            "LEFT JOIN lessons_groups ON lessons.id = lessons_groups.lesson_id " +
            "LEFT JOIN students ON lessons_groups.group_id = students.group_id " +
            "WHERE students.id = :studentId AND lessons.date = :day";
    private static final String SQL_GET_LESSONS_BY_MONTH_FOR_STUDENT = "SELECT * FROM lessons " +
            "LEFT JOIN lessons_groups ON lessons.id = lessons_groups.lesson_id " +
            "LEFT JOIN students ON lessons_groups.group_id = students.group_id " +
            "WHERE students.id = :studentId AND lessons.date >= :startDay AND lessons.date < :endDay";
    private static final String SQL_GET_LESSONS_BY_DAY_FOR_TEACHER = "SELECT * FROM lessons " +
            "WHERE lessons.teacher_id = :teacherId AND lessons.date = :day";
    private static final String SQL_GET_LESSONS_BY_MONTH_FOR_TEACHER = "SELECT * FROM lessons " +
            "WHERE lessons.teacher_id = :teacherId AND lessons.date >= :startDay AND lessons.date < :endDay";

    private final EntityManager entityManager;

    public LessonRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Lesson> findAllByTeacherIdDateAndLessonTimeId(int id, LocalDate date, int lessonTimeId) {
        logger.debug("Retrieving lessons with teacher id {}, date {} and lesson time id {}", id, date, lessonTimeId);
        return entityManager.createNativeQuery(SQL_GET_ALL_BY_TEACHER_ID_DATE_AND_TIME_ID, Lesson.class)
                .setParameter("teacherId", id)
                .setParameter("date", date)
                .setParameter("lessonTimeId", lessonTimeId)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Lesson> findAllByAudienceIdDateAndLessonTimeId(int id, LocalDate date, int lessonTimeId) {
        logger.debug("Retrieving lessons with audience id {}, date {} and lesson time id {}", id, date, lessonTimeId);
        return entityManager.createNativeQuery(SQL_GET_ALL_BY_AUDIENCE_ID_DATE_AND_TIME_ID, Lesson.class)
                .setParameter("audienceId", id)
                .setParameter("date", date)
                .setParameter("lessonTimeId", lessonTimeId)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Lesson> findByDateForStudent(int id, LocalDate day) {
        logger.debug("Retrieving lessons for date {} for student with id {}", day, id);
        return entityManager.createNativeQuery(SQL_GET_LESSONS_BY_DAY_FOR_STUDENT, Lesson.class)
                .setParameter("studentId", id)
                .setParameter("day", day)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Lesson> findByDateForTeacher(int id, LocalDate day) {
        logger.debug("Retrieving lessons for date {} for teacher with id {}", day, id);
        return entityManager.createNativeQuery(SQL_GET_LESSONS_BY_DAY_FOR_TEACHER, Lesson.class)
                .setParameter("teacherId", id)
                .setParameter("day", day)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Lesson> findByMonthForStudent(int id, LocalDate startDay) {
        logger.debug("Retrieving lessons for month from {} for student with id {}", startDay, id);
        return entityManager.createNativeQuery(SQL_GET_LESSONS_BY_MONTH_FOR_STUDENT, Lesson.class)
                .setParameter("studentId", id)
                .setParameter("startDay", startDay)
                .setParameter("endDay", startDay.plusMonths(1))
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Lesson> findByMonthForTeacher(int id, LocalDate startDay) {
        logger.debug("Retrieving lessons for month from {} for teacher with id {}", startDay, id);
        return entityManager.createNativeQuery(SQL_GET_LESSONS_BY_MONTH_FOR_TEACHER, Lesson.class)
                .setParameter("teacherId", id)
                .setParameter("startDay", startDay)
                .setParameter("endDay", startDay.plusMonths(1))
                .getResultList();
    }
}
