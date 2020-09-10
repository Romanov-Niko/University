package com.foxminded.university.dao.mapper;

import com.foxminded.university.dao.DayScheduleDao;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.domain.DaySchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DayScheduleMapper implements RowMapper<DaySchedule> {

    private final LessonDao lessonDao;

    public DayScheduleMapper(LessonDao lessonDao) {
        this.lessonDao = lessonDao;
    }

    @Override
    public DaySchedule mapRow(ResultSet resultSet, int i) throws SQLException {
        DaySchedule daySchedule = new DaySchedule();
        daySchedule.setId(resultSet.getInt("day_id"));
        daySchedule.setDay(resultSet.getDate("day").toLocalDate());
        daySchedule.setLessons(lessonDao.getAllByDayId(daySchedule.getId()));
        return daySchedule;
    }
}
