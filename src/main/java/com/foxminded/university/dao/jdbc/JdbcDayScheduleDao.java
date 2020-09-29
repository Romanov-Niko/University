package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.DayScheduleDao;
import com.foxminded.university.dao.jdbc.mapper.DayScheduleMapper;
import com.foxminded.university.domain.DaySchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcDayScheduleDao implements DayScheduleDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcDayScheduleDao.class);

    private static final String SQL_GET_SCHEDULE_BY_DAY_FOR_STUDENT = "SELECT * FROM lessons " +
            "LEFT JOIN lessons_groups ON lessons.id = lessons_groups.lesson_id " +
            "LEFT JOIN students ON lessons_groups.group_id = students.group_id " +
            "WHERE students.id = ? AND lessons.date = ?";
    private static final String SQL_GET_SCHEDULE_BY_MONTH_FOR_STUDENT = "SELECT * FROM lessons " +
            "LEFT JOIN lessons_groups ON lessons.id = lessons_groups.lesson_id " +
            "LEFT JOIN students ON lessons_groups.group_id = students.group_id " +
            "WHERE students.id = ? AND lessons.date >= ? AND lessons.date < ?";
    private static final String SQL_GET_SCHEDULE_BY_DAY_FOR_TEACHER = "SELECT * FROM lessons " +
            "WHERE lessons.teacher_id = ? AND lessons.date = ?";
    private static final String SQL_GET_SCHEDULE_BY_MONTH_FOR_TEACHER = "SELECT * FROM lessons " +
            "WHERE lessons.teacher_id = ? AND lessons.date >= ? AND lessons.date < ?";

    private final JdbcTemplate jdbcTemplate;
    private final DayScheduleMapper dayScheduleMapper;

    public JdbcDayScheduleDao(JdbcTemplate jdbcTemplate, DayScheduleMapper dayScheduleMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.dayScheduleMapper = dayScheduleMapper;
    }


    @Override
    public Optional<DaySchedule> getByDateForStudent(int id, LocalDate day) {
        logger.debug("Retrieving schedule for date {} for student with id {}", day, id);
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_GET_SCHEDULE_BY_DAY_FOR_STUDENT, dayScheduleMapper, id, day));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<DaySchedule> getByDateForTeacher(int id, LocalDate day) {
        logger.debug("Retrieving schedule for date {} for teacher with id {}", day, id);
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_GET_SCHEDULE_BY_DAY_FOR_TEACHER, dayScheduleMapper, id, day));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<DaySchedule> getByMonthForStudent(int id, LocalDate startDay) {
        logger.debug("Retrieving daily schedules for month from {} for student with id {}", startDay, id);
        return jdbcTemplate.query(SQL_GET_SCHEDULE_BY_MONTH_FOR_STUDENT, dayScheduleMapper, id, startDay, startDay.plusMonths(1));
    }

    @Override
    public List<DaySchedule> getByMonthForTeacher(int id, LocalDate startDay) {
        logger.debug("Retrieving daily schedules for month from {} for teacher with id {}", startDay, id);
        return jdbcTemplate.query(SQL_GET_SCHEDULE_BY_MONTH_FOR_TEACHER, dayScheduleMapper, id, startDay, startDay.plusMonths(1));
    }
}
