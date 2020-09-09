package com.foxminded.university.dao.mapper;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.domain.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LessonMapper implements RowMapper<Lesson> {

    @Autowired
    private SubjectDao subjectDao;

    @Autowired
    private TeacherDao teacherDao;

    @Autowired
    private AudienceDao audienceDao;

    @Autowired
    private LessonTimeDao lessonTimeDao;

    @Override
    public Lesson mapRow(ResultSet resultSet, int i) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setId(resultSet.getInt("lesson_id"));
        lesson.setSubject(subjectDao.getById(resultSet.getInt("subject_id")));
        lesson.setTeacher(teacherDao.getById(resultSet.getInt("teacher_id")));
        lesson.setAudience(audienceDao.getById(resultSet.getInt("audience_id")));
        lesson.setLessonTime(lessonTimeDao.getById(resultSet.getInt("lesson_time_id")));
        return lesson;
    }
}
