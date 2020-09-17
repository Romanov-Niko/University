package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.DayScheduleDao;
import com.foxminded.university.dao.jdbc.mapper.DayScheduleMapper;
import com.foxminded.university.domain.DaySchedule;
import com.foxminded.university.domain.Lesson;
import com.foxminded.university.domain.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
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

    private final JdbcTemplate jdbcTemplate;
    private final DayScheduleMapper dayScheduleMapper;

    @Autowired
    private JdbcLessonDao jdbcLessonDao;

    public JdbcDayScheduleDao(JdbcTemplate jdbcTemplate, DayScheduleMapper dayScheduleMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.dayScheduleMapper = dayScheduleMapper;
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
        List<Lesson> oldLessons = jdbcLessonDao.getAllByDayId(daySchedule.getId());
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

    private void removeLessonFromDaySchedule (int dayId, int lessonId) {
        jdbcTemplate.update(SQL_DELETE_LESSON_FROM_DAY, dayId, lessonId);
    }

    private void saveLessonToDaySchedule (int dayId, int lessonId) {
        jdbcTemplate.update(SQL_SAVE_LESSON_TO_DAY, dayId, lessonId);
    }
}
