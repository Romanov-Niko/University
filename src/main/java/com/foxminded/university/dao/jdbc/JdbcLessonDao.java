package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.mapper.AudienceMapper;
import com.foxminded.university.dao.mapper.GroupMapper;
import com.foxminded.university.dao.mapper.LessonMapper;
import com.foxminded.university.dao.mapper.SubjectMapper;
import com.foxminded.university.domain.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.sql.Date;

@Component
public class JdbcLessonDao implements LessonDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_LESSON_BY_ID = "SELECT * FROM lessons WHERE lesson_id = ?";
    private static final String SQL_GET_ALL_LESSONS = "SELECT * FROM lessons";
    private static final String SQL_SAVE_LESSON = "INSERT INTO lessons VALUES (DEFAULT, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_LESSON = "UPDATE lessons SET subject_id = ?, teacher_id = ?, audience_id = ?, lesson_time_id = ? WHERE lesson_id = ?";
    private static final String SQL_DELETE_LESSON = "DELETE FROM lessons WHERE lesson_id = ?";
    private static final String SQL_GET_ALL_LESSONS_BY_DAY_ID = "SELECT days_lessons.lesson_id, lessons.subject_id, lessons.teacher_id, lessons.audience_id, lessons.lesson_time_id " +
            "FROM days_lessons " +
            "LEFT JOIN days ON days_lessons.day_id = days.day_id " +
            "LEFT JOIN lessons ON days_lessons.lesson_id = lessons.lesson_id " +
            "WHERE days.day_id = ?";

    @Autowired
    public JdbcLessonDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Lesson getById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_LESSON_BY_ID, new LessonMapper(new JdbcSubjectDao(jdbcTemplate.getDataSource()),
                new JdbcTeacherDao(jdbcTemplate.getDataSource()), new JdbcAudienceDao(jdbcTemplate.getDataSource(),
                new AudienceMapper()), new JdbcLessonTimeDao(jdbcTemplate.getDataSource()), new JdbcGroupDao(jdbcTemplate.getDataSource())), id);
    }

    @Override
    public List<Lesson> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_LESSONS, new LessonMapper(new JdbcSubjectDao(jdbcTemplate.getDataSource()),
                new JdbcTeacherDao(jdbcTemplate.getDataSource()), new JdbcAudienceDao(jdbcTemplate.getDataSource(),
                new AudienceMapper()), new JdbcLessonTimeDao(jdbcTemplate.getDataSource()), new JdbcGroupDao(jdbcTemplate.getDataSource())));
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
        lesson.setId((int) keyHolder.getKeys().get("lesson_id"));
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
        return jdbcTemplate.query(SQL_GET_ALL_LESSONS_BY_DAY_ID, new LessonMapper(new JdbcSubjectDao(jdbcTemplate.getDataSource()),
                new JdbcTeacherDao(jdbcTemplate.getDataSource()), new JdbcAudienceDao(jdbcTemplate.getDataSource(),
                new AudienceMapper()), new JdbcLessonTimeDao(jdbcTemplate.getDataSource()), new JdbcGroupDao(jdbcTemplate.getDataSource())), id);
    }
}
