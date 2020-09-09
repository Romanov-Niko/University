package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.DayScheduleDao;
import com.foxminded.university.dao.mapper.AudienceMapper;
import com.foxminded.university.dao.mapper.DayScheduleMapper;
import com.foxminded.university.domain.DaySchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class JdbcDayScheduleDao implements DayScheduleDao {

    JdbcTemplate jdbcTemplate;
    private DayScheduleMapper dayScheduleMapper;

    private static final String SQL_GET_DAY_SCHEDULE_BY_ID = "SELECT * FROM days WHERE day_id = ?";
    private static final String SQL_GET_ALL_DAY_SCHEDULES = "SELECT * FROM days";
    private static final String SQL_SAVE_DAY_SCHEDULE = "INSERT INTO days VALUES (DEFAULT, ?)";
    private static final String SQL_UPDATE_DAY_SCHEDULE = "UPDATE days SET day = ? WHERE day_id = ?";
    private static final String SQL_DELETE_DAY_SCHEDULE = "DELETE FROM days WHERE day_id = ?";

    @Autowired
    public JdbcDayScheduleDao(DataSource dataSource, DayScheduleMapper dayScheduleMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.dayScheduleMapper = dayScheduleMapper;
    }

    @Override
    public DaySchedule getById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_DAY_SCHEDULE_BY_ID, dayScheduleMapper, id);
    }

    @Override
    public List<DaySchedule> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_DAY_SCHEDULES, dayScheduleMapper);
    }

    @Override
    public void save(DaySchedule daySchedule) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_DAY_SCHEDULE, Statement.RETURN_GENERATED_KEYS);
            statement.setObject(1, daySchedule.getDay());
            return statement;
        }, keyHolder);
        daySchedule.setId((int)keyHolder.getKeys().get("day_id"));
    }

    @Override
    public void update(DaySchedule daySchedule) {
        jdbcTemplate.update(SQL_UPDATE_DAY_SCHEDULE, daySchedule.getDay(), daySchedule.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_DAY_SCHEDULE, id);
    }
}
