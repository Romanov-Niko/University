package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.dao.jdbc.mapper.LessonTimeMapper;
import com.foxminded.university.domain.LessonTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class JdbcLessonTimeDao implements LessonTimeDao {

    private static final String SQL_GET_LESSON_TIME_BY_ID = "SELECT * FROM lessons_times WHERE id = ?";
    private static final String SQL_GET_ALL_LESSONS_TIMES = "SELECT * FROM lessons_times";
    private static final String SQL_SAVE_LESSON_TIME = "INSERT INTO lessons_times VALUES (DEFAULT, ?, ?)";
    private static final String SQL_UPDATE_LESSON_TIME = "UPDATE lessons_times SET begin_time = ?, end_time = ? WHERE id = ?";
    private static final String SQL_DELETE_LESSON_TIME = "DELETE FROM lessons_times WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final LessonTimeMapper lessonTimeMapper;

    @Autowired
    public JdbcLessonTimeDao(JdbcTemplate jdbcTemplate, LessonTimeMapper lessonTimeMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.lessonTimeMapper = lessonTimeMapper;
    }

    @Override
    public LessonTime getById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_LESSON_TIME_BY_ID, lessonTimeMapper, id);
    }

    @Override
    public List<LessonTime> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_LESSONS_TIMES, lessonTimeMapper);
    }

    @Override
    public void save(LessonTime lessonTime) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_LESSON_TIME, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(1, lessonTime.getBegin());
            statement.setObject(2, lessonTime.getEnd());
            return statement;
        }, keyHolder);
        lessonTime.setId((int) keyHolder.getKeys().get("id"));
    }

    @Override
    public void update(LessonTime lessonTime) {
        jdbcTemplate.update(SQL_UPDATE_LESSON_TIME, lessonTime.getBegin(), lessonTime.getEnd(), lessonTime.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_LESSON_TIME, id);
    }
}
