package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.DayScheduleDao;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.jdbc.mapper.DayScheduleMapper;
import com.foxminded.university.domain.DaySchedule;
import com.foxminded.university.domain.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

@Component
public class JdbcDayScheduleDao implements DayScheduleDao {

    private static final String SQL_GET_DAY_SCHEDULE_BY_ID = "SELECT * FROM days_schedules WHERE id = ?";
    private static final String SQL_GET_ALL_DAY_SCHEDULES = "SELECT * FROM days_schedules";
    private static final String SQL_SAVE_DAY_SCHEDULE = "INSERT INTO days_schedules VALUES (DEFAULT, ?)";
    private static final String SQL_UPDATE_DAY_SCHEDULE = "UPDATE days_schedules SET day = ? WHERE id = ?";
    private static final String SQL_DELETE_DAY_SCHEDULE = "DELETE FROM days_schedules WHERE id = ?";
    private static final String SQL_DELETE_LESSON_FROM_DAY = "DELETE FROM days_lessons WHERE day_id = ? AND lesson_id = ?";
    private static final String SQL_SAVE_LESSON_TO_DAY = "INSERT INTO days_lessons VALUES (?, ?)";
    private static final String SQL_GET_SCHEDULE_BY_DAY_FOR_STUDENT = "SELECT * FROM days_schedules " +
            "LEFT JOIN days_lessons ON days_schedules.id = days_lessons.day_id " +
            "LEFT JOIN lessons_groups ON days_lessons.lesson_id = lessons_groups.lesson_id " +
            "LEFT JOIN students ON lessons_groups.group_id = students.group_id " +
            "WHERE students.id = ? AND days_schedules.day = ?";
    private static final String SQL_GET_SCHEDULE_BY_MONTH_FOR_STUDENT = "SELECT * FROM days_schedules " +
            "LEFT JOIN days_lessons ON days_schedules.id = days_lessons.day_id " +
            "LEFT JOIN lessons_groups ON days_lessons.lesson_id = lessons_groups.lesson_id " +
            "LEFT JOIN students ON lessons_groups.group_id = students.group_id " +
            "WHERE students.id = ? AND days_schedules.day >= ? AND days_schedules.day < ?";
    private static final String SQL_GET_SCHEDULE_BY_DAY_FOR_TEACHER = "SELECT * FROM days_schedules " +
            "LEFT JOIN days_lessons ON days_schedules.id = days_lessons.day_id " +
            "LEFT JOIN lessons ON days_lessons.lesson_id = lessons.id " +
            "WHERE lessons.teacher_id = ? AND days_schedules.day = ?";
    private static final String SQL_GET_SCHEDULE_BY_MONTH_FOR_TEACHER = "SELECT * FROM days_schedules " +
            "LEFT JOIN days_lessons ON days_schedules.id = days_lessons.day_id " +
            "LEFT JOIN lessons ON days_lessons.lesson_id = lessons.id " +
            "WHERE lessons.teacher_id = ? AND days_schedules.day >= ? AND days_schedules.day < ?";

    private final JdbcTemplate jdbcTemplate;
    private final DayScheduleMapper dayScheduleMapper;
    private final LessonDao lessonDao;

    @Autowired
    public JdbcDayScheduleDao(JdbcTemplate jdbcTemplate, DayScheduleMapper dayScheduleMapper, LessonDao lessonDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.dayScheduleMapper = dayScheduleMapper;
        this.lessonDao = lessonDao;
    }

    @Override
    public DaySchedule getById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_DAY_SCHEDULE_BY_ID, dayScheduleMapper, id);
    }

    @Override
    public List<DaySchedule> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_DAY_SCHEDULES, dayScheduleMapper);
    }

    @Transactional
    @Override
    public void save(DaySchedule daySchedule) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_DAY_SCHEDULE, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(1, daySchedule.getDay());
            return statement;
        }, keyHolder);
        daySchedule.setId((int) keyHolder.getKeys().get("id"));
        daySchedule.getLessons().forEach(lesson -> saveLessonToDaySchedule(daySchedule.getId(), lesson.getId()));
    }

    @Transactional
    @Override
    public void update(DaySchedule daySchedule) {
        List<Lesson> oldLessons = lessonDao.getAllByDayId(daySchedule.getId());
        oldLessons.stream()
                .filter(lesson -> !daySchedule.getLessons().contains(lesson))
                .forEach(lesson -> removeLessonFromDaySchedule(daySchedule.getId(), lesson.getId()));

        daySchedule.getLessons().stream()
                .filter(lesson -> !oldLessons.contains(lesson))
                .forEach(lesson -> saveLessonToDaySchedule(daySchedule.getId(), lesson.getId()));

        jdbcTemplate.update(SQL_UPDATE_DAY_SCHEDULE, daySchedule.getDay(), daySchedule.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_DAY_SCHEDULE, id);
    }

    @Override
    public DaySchedule getByDayForStudent(int id, LocalDate day) {
        return jdbcTemplate.queryForObject(SQL_GET_SCHEDULE_BY_DAY_FOR_STUDENT, dayScheduleMapper, id, day);
    }

    @Override
    public DaySchedule getByDayForTeacher(int id, LocalDate day) {
        return jdbcTemplate.queryForObject(SQL_GET_SCHEDULE_BY_DAY_FOR_TEACHER, dayScheduleMapper, id, day);
    }

    @Override
    public List<DaySchedule> getByMonthForStudent(int id, LocalDate startDay) {
        return jdbcTemplate.query(SQL_GET_SCHEDULE_BY_MONTH_FOR_STUDENT, dayScheduleMapper, id, startDay, startDay.plusMonths(1));
    }

    @Override
    public List<DaySchedule> getByMonthForTeacher(int id, LocalDate startDay) {
        return jdbcTemplate.query(SQL_GET_SCHEDULE_BY_MONTH_FOR_TEACHER, dayScheduleMapper, id, startDay, startDay.plusMonths(1));
    }

    private void removeLessonFromDaySchedule(int dayId, int lessonId) {
        jdbcTemplate.update(SQL_DELETE_LESSON_FROM_DAY, dayId, lessonId);
    }

    private void saveLessonToDaySchedule(int dayId, int lessonId) {
        jdbcTemplate.update(SQL_SAVE_LESSON_TO_DAY, dayId, lessonId);
    }
}
