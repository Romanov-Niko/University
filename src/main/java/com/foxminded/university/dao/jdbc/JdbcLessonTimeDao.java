package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.dao.mapper.GroupMapper;
import com.foxminded.university.dao.mapper.LessonTimeMapper;
import com.foxminded.university.domain.LessonTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.sql.Time;

@Component
public class JdbcLessonTimeDao implements LessonTimeDao {

    JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_LESSON_TIME_BY_ID = "SELECT * FROM lessons_times WHERE lesson_time_id = ?";
    private static final String SQL_GET_ALL_LESSONS_TIMES = "SELECT * FROM lessons_times";
    private static final String SQL_SAVE_LESSON_TIME = "INSERT INTO lessons_times VALUES (DEFAULT, ?, ?)";
    private static final String SQL_UPDATE_LESSON_TIME = "UPDATE lessons_times SET begin_time = ?, end_time = ? WHERE lesson_time_id = ?";
    private static final String SQL_DELETE_LESSON_TIME = "DELETE FROM lessons_times WHERE lesson_time_id = ?";

    @Autowired
    public JdbcLessonTimeDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public LessonTime getById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_LESSON_TIME_BY_ID, new LessonTimeMapper(), id);
    }

    @Override
    public List<LessonTime> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_LESSONS_TIMES, new LessonTimeMapper());
    }

    @Override
    public void save(LessonTime lessonTime) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_LESSON_TIME, Statement.RETURN_GENERATED_KEYS);
            statement.setTime(1, Time.valueOf(lessonTime.getBegin()));
            statement.setTime(2, Time.valueOf(lessonTime.getEnd()));
            return statement;
        }, keyHolder);
        lessonTime.setId((int)keyHolder.getKeys().get("lesson_time_id"));
    }

    @Override
    public void update(LessonTime lessonTime) {
        jdbcTemplate.update(SQL_UPDATE_LESSON_TIME, Time.valueOf(lessonTime.getBegin()), Time.valueOf(lessonTime.getEnd()), lessonTime.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_LESSON_TIME, id);
    }
}
