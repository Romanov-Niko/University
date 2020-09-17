package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.jdbc.mapper.LessonMapper;
import com.foxminded.university.domain.Group;
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
public class JdbcLessonDao implements LessonDao {

    private static final String SQL_GET_LESSON_BY_ID = "SELECT * FROM lessons WHERE id = ?";
    private static final String SQL_GET_ALL_LESSONS = "SELECT * FROM lessons";
    private static final String SQL_SAVE_LESSON = "INSERT INTO lessons VALUES (DEFAULT, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_LESSON = "UPDATE lessons SET subject_id = ?, teacher_id = ?, audience_id = ?, lesson_time_id = ? WHERE id = ?";
    private static final String SQL_DELETE_LESSON = "DELETE FROM lessons WHERE id = ?";
    private static final String SQL_GET_ALL_LESSONS_BY_DAY_ID = "SELECT days_lessons.lesson_id, lessons.id, lessons.subject_id, " +
            "lessons.teacher_id, lessons.audience_id, lessons.lesson_time_id " +
            "FROM days_lessons " +
            "LEFT JOIN days_schedules ON days_lessons.day_id = days_schedules.id " +
            "LEFT JOIN lessons ON days_lessons.lesson_id = lessons.id " +
            "WHERE days_lessons.day_id = ?";
    private static final String SQL_DELETE_GROUP_FROM_LESSON = "DELETE FROM lessons_groups WHERE lesson_id = ? AND group_id = ?";
    private static final String SQL_SAVE_GROUP_TO_LESSON = "INSERT INTO lessons_groups VALUES (?, ?)";

    private final JdbcTemplate jdbcTemplate;
    private final LessonMapper lessonMapper;

    @Autowired
    private JdbcGroupDao jdbcGroupDao;

    public JdbcLessonDao(JdbcTemplate jdbcTemplate, LessonMapper lessonMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.lessonMapper = lessonMapper;
    }

    @Override
    public Lesson getById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_LESSON_BY_ID, lessonMapper, id);
    }

    @Override
    public List<Lesson> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_LESSONS, lessonMapper);
    }

    @Transactional
    @Override
    public void save(Lesson lesson) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_LESSON, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, lesson.getSubject().getId());
            statement.setInt(2, lesson.getTeacher().getId());
            statement.setInt(3, lesson.getAudience().getId());
            statement.setInt(4, lesson.getLessonTime().getId());
            return statement;
        }, keyHolder);
        lesson.setId((int) keyHolder.getKeys().get("id"));
        lesson.getGroups().forEach(group -> saveGroupToLesson(lesson.getId(), group.getId()));
    }

    @Transactional
    @Override
    public void update(Lesson lesson) {
        List<Group> oldGroups = jdbcGroupDao.getAllByLessonId(lesson.getId());
        oldGroups.stream()
                .filter(group -> !lesson.getGroups().contains(group))
                .forEach(group -> removeGroupFromLesson(lesson.getId(), group.getId()));

        lesson.getGroups().stream()
                .filter(group -> !oldGroups.contains(group))
                .forEach(group -> saveGroupToLesson(lesson.getId(), group.getId()));

        jdbcTemplate.update(SQL_UPDATE_LESSON, lesson.getSubject().getId(), lesson.getTeacher().getId(), lesson.getAudience().getId(),
                lesson.getLessonTime().getId(), lesson.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_LESSON, id);
    }

    @Override
    public List<Lesson> getAllByDayId(int id) {
        return jdbcTemplate.query(SQL_GET_ALL_LESSONS_BY_DAY_ID, lessonMapper, id);
    }

    private void removeGroupFromLesson (int lessonId, int groupId) {
        jdbcTemplate.update(SQL_DELETE_GROUP_FROM_LESSON, lessonId, groupId);
    }

    private void saveGroupToLesson (int lessonId, int groupId) {
        jdbcTemplate.update(SQL_SAVE_GROUP_TO_LESSON, lessonId, groupId);
    }
}
