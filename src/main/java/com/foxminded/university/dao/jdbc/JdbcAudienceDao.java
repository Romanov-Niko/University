package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.dao.mapper.AudienceMapper;
import com.foxminded.university.domain.Audience;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class JdbcAudienceDao implements AudienceDao {

    private static final String SQL_GET_AUDIENCE_BY_ID = "SELECT * FROM audiences WHERE id = ?";
    private static final String SQL_GET_ALL_AUDIENCES = "SELECT * FROM audiences";
    private static final String SQL_SAVE_AUDIENCE = "INSERT INTO audiences VALUES (DEFAULT, ?, ?)";
    private static final String SQL_UPDATE_AUDIENCE = "UPDATE audiences SET room_number = ?, capacity = ? WHERE id = ?";
    private static final String SQL_DELETE_AUDIENCE = "DELETE FROM audiences WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final AudienceMapper audienceMapper;

    public JdbcAudienceDao(JdbcTemplate jdbcTemplate, AudienceMapper audienceMapper) {
        this.audienceMapper = audienceMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Audience getById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_AUDIENCE_BY_ID, audienceMapper, id);
    }

    @Override
    public List<Audience> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL_AUDIENCES, audienceMapper);
    }

    @Override
    public void save(Audience audience) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_AUDIENCE, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, audience.getRoomNumber());
            statement.setInt(2, audience.getCapacity());
            return statement;
        }, keyHolder);
        audience.setId((int) keyHolder.getKeys().get("id"));
    }

    @Override
    public void update(Audience audience) {
        jdbcTemplate.update(SQL_UPDATE_AUDIENCE, audience.getRoomNumber(), audience.getCapacity(), audience.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_AUDIENCE, id);
    }
}
