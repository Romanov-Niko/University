package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.mapper.AudienceMapper;
import com.foxminded.university.dao.mapper.LessonMapper;
import com.foxminded.university.domain.Lesson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

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

    private final JdbcTemplate jdbcTemplate;

    public JdbcLessonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Lesson getById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_LESSON_BY_ID, new LessonMapper(new JdbcSubjectDao(jdbcTemplate),
                new JdbcTeacherDao(jdbcTemplate), new JdbcAudienceDao(jdbcTemplate,
                new AudienceMapper()), new JdbcLessonTimeDao(jdbcTemplate), new JdbcGroupDao(jdbcTemplate)), id);
    }

    @Override
    public List<Lesson> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_LESSONS, new LessonMapper(new JdbcSubjectDao(jdbcTemplate),
                new JdbcTeacherDao(jdbcTemplate), new JdbcAudienceDao(jdbcTemplate,
                new AudienceMapper()), new JdbcLessonTimeDao(jdbcTemplate), new JdbcGroupDao(jdbcTemplate)));
    }

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
    }

    @Override
    public void update(Lesson lesson) {
        jdbcTemplate.update(SQL_UPDATE_LESSON, lesson.getSubject().getId(), lesson.getTeacher().getId(), lesson.getAudience().getId(),
                lesson.getLessonTime().getId(), lesson.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_LESSON, id);
    }

    @Override
    public List<Lesson> getAllByDayId(int id) {
        return jdbcTemplate.query(SQL_GET_ALL_LESSONS_BY_DAY_ID, new LessonMapper(new JdbcSubjectDao(jdbcTemplate),
                new JdbcTeacherDao(jdbcTemplate), new JdbcAudienceDao(jdbcTemplate,
                new AudienceMapper()), new JdbcLessonTimeDao(jdbcTemplate), new JdbcGroupDao(jdbcTemplate)), id);
    }
}
