package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.DayScheduleDao;
import com.foxminded.university.domain.DaySchedule;
import com.foxminded.university.domain.Lesson;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Transactional
@Repository
public class JdbcDayScheduleDao implements DayScheduleDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcDayScheduleDao.class);

    private static final String SQL_GET_SCHEDULE_BY_DAY_FOR_STUDENT = "SELECT * FROM lessons " +
            "LEFT JOIN lessons_groups ON lessons.id = lessons_groups.lesson_id " +
            "LEFT JOIN students ON lessons_groups.group_id = students.group_id " +
            "WHERE students.id = :studentId AND lessons.date = :day";
    private static final String SQL_GET_SCHEDULE_BY_MONTH_FOR_STUDENT = "SELECT * FROM lessons " +
            "LEFT JOIN lessons_groups ON lessons.id = lessons_groups.lesson_id " +
            "LEFT JOIN students ON lessons_groups.group_id = students.group_id " +
            "WHERE students.id = :studentId AND lessons.date >= :startDay AND lessons.date < :endDay";
    private static final String SQL_GET_SCHEDULE_BY_DAY_FOR_TEACHER = "SELECT * FROM lessons " +
            "WHERE lessons.teacher_id = :teacherId AND lessons.date = :day";
    private static final String SQL_GET_SCHEDULE_BY_MONTH_FOR_TEACHER = "SELECT * FROM lessons " +
            "WHERE lessons.teacher_id = :teacherId AND lessons.date >= :startDay AND lessons.date < :endDay";

    private final SessionFactory sessionFactory;

    public JdbcDayScheduleDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<DaySchedule> getByDateForStudent(int id, LocalDate day) {
        logger.debug("Retrieving schedule for date {} for student with id {}", day, id);
        try {
            return Optional.of(new DaySchedule(day, sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_SCHEDULE_BY_DAY_FOR_STUDENT, Lesson.class)
                    .setParameter("studentId", id)
                    .setParameter("day", day)
                    .getResultList()));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<DaySchedule> getByDateForTeacher(int id, LocalDate day) {
        logger.debug("Retrieving schedule for date {} for teacher with id {}", day, id);
        try {
            return Optional.of(new DaySchedule(day, sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_SCHEDULE_BY_DAY_FOR_TEACHER, Lesson.class)
                    .setParameter("teacherId", id)
                    .setParameter("day", day)
                    .getResultList()));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<DaySchedule> getByMonthForStudent(int id, LocalDate startDay) {
        logger.debug("Retrieving daily schedules for month from {} for student with id {}", startDay, id);
        List<Lesson> lessons = sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_SCHEDULE_BY_MONTH_FOR_STUDENT, Lesson.class)
                .setParameter("studentId", id)
                .setParameter("startDay", startDay)
                .setParameter("endDay", startDay.plusMonths(1))
                .getResultList();
        return getSchedules(lessons);
    }

    @Override
    public List<DaySchedule> getByMonthForTeacher(int id, LocalDate startDay) {
        logger.debug("Retrieving daily schedules for month from {} for teacher with id {}", startDay, id);
        List<Lesson> lessons = sessionFactory.getCurrentSession().createNativeQuery(SQL_GET_SCHEDULE_BY_MONTH_FOR_TEACHER, Lesson.class)
                .setParameter("teacherId", id)
                .setParameter("startDay", startDay)
                .setParameter("endDay", startDay.plusMonths(1))
                .getResultList();
        return getSchedules(lessons);
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
