package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.jdbc.mapper.LessonMapper;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.DaySchedule;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Lesson;
import com.foxminded.university.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcLessonDao implements LessonDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcLessonDao.class);

    private static final String SQL_GET_LESSON_BY_ID = "SELECT * FROM lessons WHERE id = ?";
    private static final String SQL_GET_ALL_LESSONS = "SELECT * FROM lessons";
    private static final String SQL_SAVE_LESSON = "INSERT INTO lessons VALUES (DEFAULT, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_LESSON = "UPDATE lessons SET subject_id = ?, teacher_id = ?, audience_id = ?, " +
            "lesson_time_id = ?, date = ? WHERE id = ?";
    private static final String SQL_DELETE_LESSON = "DELETE FROM lessons WHERE id = ?";
    private static final String SQL_GET_ALL_LESSONS_BY_DAY = "SELECT * FROM lessons WHERE date = ?";
    private static final String SQL_DELETE_GROUP_FROM_LESSON = "DELETE FROM lessons_groups WHERE lesson_id = ? AND group_id = ?";
    private static final String SQL_SAVE_GROUP_TO_LESSON = "INSERT INTO lessons_groups VALUES (?, ?)";
    private static final String SQL_GET_ALL_BY_TEACHER_ID_DATE_AND_TIME_ID = "SELECT * FROM lessons " +
            "WHERE teacher_id = ? AND date = ? AND lesson_time_id = ?";
    private static final String SQL_GET_ALL_BY_AUDIENCE_ID_DATE_AND_TIME_ID = "SELECT * FROM lessons " +
            "WHERE audience_id = ? AND date = ? AND lesson_time_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final LessonMapper lessonMapper;
    private final GroupDao groupDao;

    public JdbcLessonDao(JdbcTemplate jdbcTemplate, LessonMapper lessonMapper, GroupDao groupDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.lessonMapper = lessonMapper;
        this.groupDao = groupDao;
    }

    @Override
    public Optional<Lesson> getById(int id) {
        logger.debug("Retrieving lesson with id {}", id);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_LESSON_BY_ID, lessonMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            logger.error("Lesson with id {} is not present", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Lesson> getAll() {
        logger.debug("Retrieved all lessons");
        return jdbcTemplate.query(SQL_GET_ALL_LESSONS, lessonMapper);
    }

    @Transactional
    @Override
    public void save(Lesson lesson) {
        logger.debug("Saving lesson");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_LESSON, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, lesson.getSubject().getId());
            statement.setInt(2, lesson.getTeacher().getId());
            statement.setInt(3, lesson.getAudience().getId());
            statement.setInt(4, lesson.getLessonTime().getId());
            statement.setObject(5, lesson.getDate());
            return statement;
        }, keyHolder) == 0) {
            throw new EntityNotSavedException("Lesson was not saved");
        }
        lesson.setId((int) Objects.requireNonNull(keyHolder.getKeys()).get("id"));
        lesson.getGroups().forEach(group -> saveGroupToLesson(lesson.getId(), group.getId()));
    }

    @Transactional
    @Override
    public void update(Lesson lesson) {
        logger.debug("Updating lesson with id {}", lesson.getId());
        if (jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_LESSON);
            statement.setInt(1, lesson.getSubject().getId());
            statement.setInt(2, lesson.getTeacher().getId());
            statement.setInt(3, lesson.getAudience().getId());
            statement.setInt(4, lesson.getLessonTime().getId());
            statement.setObject(5, lesson.getDate());
            statement.setInt(6, lesson.getId());

            List<Group> oldGroups = groupDao.getAllByLessonId(lesson.getId());
            oldGroups.stream()
                    .filter(group -> !lesson.getGroups().contains(group))
                    .forEach(group -> removeGroupFromLesson(lesson.getId(), group.getId()));

            lesson.getGroups().stream()
                    .filter(group -> !oldGroups.contains(group))
                    .forEach(group -> saveGroupToLesson(lesson.getId(), group.getId()));

            return statement;
        }) == 0) {
            throw new EntityNotUpdatedException(String.format("Lesson with id %d was not updated", lesson.getId()));
        }
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting lesson with id {}", id);
        if (jdbcTemplate.update(SQL_DELETE_LESSON, id) == 0) {
            throw new EntityNotDeletedException(String.format("Lesson with id %d was not deleted", id));
        }
    }

    @Override
    public List<Lesson> getAllByDate(LocalDate date) {
        logger.debug("Retrieving lessons for date {}", date);
        return jdbcTemplate.query(SQL_GET_ALL_LESSONS_BY_DAY, lessonMapper, date);
    }

    @Override
    public List<Lesson> getAllByTeacherIdDateAndLessonTimeId(int id, LocalDate date, int lessonTimeId) {
        logger.debug("Retrieving lessons with teacher id {}, date {} and lesson time id {}", id, date, lessonTimeId);
        return jdbcTemplate.query(SQL_GET_ALL_BY_TEACHER_ID_DATE_AND_TIME_ID, lessonMapper, id, date, lessonTimeId);
    }

    @Override
    public List<Lesson> getAllByAudienceIdDateAndLessonTimeId(int id, LocalDate date, int lessonTimeId) {
        logger.debug("Retrieving lessons with audience id {}, date {} and lesson time id {}", id, date, lessonTimeId);
        return jdbcTemplate.query(SQL_GET_ALL_BY_AUDIENCE_ID_DATE_AND_TIME_ID, lessonMapper, id, date, lessonTimeId);
    }

    private void removeGroupFromLesson(int lessonId, int groupId) {
        logger.debug("Removing group with id {} from lesson with id {}", groupId, lessonId);
        if (jdbcTemplate.update(SQL_DELETE_GROUP_FROM_LESSON, lessonId, groupId) == 0) {
            throw new GroupNotRemovedFromLessonException(String.format("Group with id %d was not removed from lesson with id %d", groupId, lessonId));
        }
    }

    private void saveGroupToLesson(int lessonId, int groupId) {
        logger.debug("Adding group with id {} to lesson with id {}", groupId, lessonId);
        if (jdbcTemplate.update(SQL_SAVE_GROUP_TO_LESSON, lessonId, groupId) == 0) {
            throw new GroupNotAddedToLessonException(String.format("Group with id %d was not added to lesson with id %d", groupId, lessonId));
        }
    }
}
