package com.foxminded.university.dao.mapper;

import com.foxminded.university.domain.LessonTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LessonTimeMapper implements RowMapper<LessonTime> {

    @Override
    public LessonTime mapRow(ResultSet resultSet, int i) throws SQLException {
        LessonTime lessonTime = new LessonTime();
        lessonTime.setId(resultSet.getInt("id"));
        lessonTime.setBegin(resultSet.getTime("begin_time").toLocalTime());
        lessonTime.setEnd(resultSet.getTime("end_time").toLocalTime());
        return lessonTime;
    }
}
