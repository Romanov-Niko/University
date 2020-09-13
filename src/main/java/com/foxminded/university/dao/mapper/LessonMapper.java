package com.foxminded.university.dao.mapper;

import com.foxminded.university.dao.*;
import com.foxminded.university.domain.Lesson;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LessonMapper implements RowMapper<Lesson> {

    private final SubjectDao subjectDao;
    private final TeacherDao teacherDao;
    private final AudienceDao audienceDao;
    private final LessonTimeDao lessonTimeDao;
    private final GroupDao groupDao;

    public LessonMapper(SubjectDao subjectDao, TeacherDao teacherDao, AudienceDao audienceDao, LessonTimeDao lessonTimeDao, GroupDao groupDao) {
        this.subjectDao = subjectDao;
        this.teacherDao = teacherDao;
        this.audienceDao = audienceDao;
        this.lessonTimeDao = lessonTimeDao;
        this.groupDao = groupDao;
    }

    @Override
    public Lesson mapRow(ResultSet resultSet, int i) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setId(resultSet.getInt("id"));
        lesson.setSubject(subjectDao.getById(resultSet.getInt("subject_id")));
        lesson.setTeacher(teacherDao.getById(resultSet.getInt("teacher_id")));
        lesson.setGroups(groupDao.getAllByLessonId(lesson.getId()));
        lesson.setAudience(audienceDao.getById(resultSet.getInt("audience_id")));
        lesson.setLessonTime(lessonTimeDao.getById(resultSet.getInt("lesson_time_id")));
        return lesson;
    }
}
