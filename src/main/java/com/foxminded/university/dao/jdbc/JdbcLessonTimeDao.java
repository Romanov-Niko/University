package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.dao.jdbc.mapper.LessonTimeMapper;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.LessonTime;
import com.foxminded.university.exception.EntityNotDeletedException;
import com.foxminded.university.exception.EntityNotSavedException;
import com.foxminded.university.exception.EntityNotUpdatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcLessonTimeDao implements LessonTimeDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcLessonTimeDao.class);

    private static final String SQL_GET_LESSON_TIME_BY_ID = "SELECT * FROM lessons_times WHERE id = ?";
    private static final String SQL_GET_ALL_LESSONS_TIMES = "SELECT * FROM lessons_times";
    private static final String SQL_SAVE_LESSON_TIME = "INSERT INTO lessons_times VALUES (DEFAULT, ?, ?)";
    private static final String SQL_UPDATE_LESSON_TIME = "UPDATE lessons_times SET begin_time = ?, end_time = ? WHERE id = ?";
    private static final String SQL_DELETE_LESSON_TIME = "DELETE FROM lessons_times WHERE id = ?";
    private static final String SQL_GET_LESSON_TIME_BY_START_AND_END_TIME = "SELECT * FROM lessons_times WHERE begin_time = ? AND end_time = ?";


    private final JdbcTemplate jdbcTemplate;
    private final LessonTimeMapper lessonTimeMapper;

    public JdbcLessonTimeDao(JdbcTemplate jdbcTemplate, LessonTimeMapper lessonTimeMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.lessonTimeMapper = lessonTimeMapper;
    }

    @Override
    public Optional<LessonTime> getById(int id) {
        logger.debug("Retrieving lesson time with id {}", id);
        try {
            Optional<LessonTime> lessonTime = Optional.of(jdbcTemplate.queryForObject(SQL_GET_LESSON_TIME_BY_ID, lessonTimeMapper, id));
            logger.debug("Lesson time was retrieved");
            return lessonTime;
        } catch (EmptyResultDataAccessException exception) {
            logger.error("Lesson time is not present");
            return Optional.empty();
        }
    }

    @Override
    public List<LessonTime> getAll() {
        logger.debug("Retrieved all audiences");
        return jdbcTemplate.query(SQL_GET_ALL_LESSONS_TIMES, lessonTimeMapper);
    }

    @Override
    public void save(LessonTime lessonTime) {
        logger.debug("Saving lesson");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_LESSON_TIME, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(1, lessonTime.getBegin());
            statement.setObject(2, lessonTime.getEnd());
            return statement;
        }, keyHolder) == 0) {
            throw new EntityNotSavedException("Lesson time was not saved");
        } else {
            lessonTime.setId((int) keyHolder.getKeys().get("id"));
            logger.debug("Lesson time was saved");
        }
    }

    @Override
    public void update(LessonTime lessonTime) {
        logger.debug("Updating lesson  time with id {}", lessonTime.getId());
        if (jdbcTemplate.update(SQL_UPDATE_LESSON_TIME, lessonTime.getBegin(), lessonTime.getEnd(), lessonTime.getId()) == 0) {
            throw new EntityNotUpdatedException("Lesson time was not updated");
        } else {
            logger.debug("Lesson time was updated");
        }
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting lesson time with id {}", id);
        if (jdbcTemplate.update(SQL_DELETE_LESSON_TIME, id) == 0) {
            throw new EntityNotDeletedException("Lesson time was not deleted");
        } else {
            logger.debug("Lesson time was deleted");
        }
    }

    @Override
    public Optional<LessonTime> getByStartAndEndTime(LocalTime start, LocalTime end) {
        logger.debug("Retrieving lesson time with start {} and end {}", start, end);
        try {
            Optional<LessonTime> lessonTime = Optional.of(jdbcTemplate.queryForObject(SQL_GET_LESSON_TIME_BY_START_AND_END_TIME, lessonTimeMapper, start, end));
            logger.debug("Lesson time was retrieved");
            return lessonTime;
        } catch (EmptyResultDataAccessException exception) {
            logger.error("Lesson time is not present");
            return Optional.empty();
        }
    }
}
