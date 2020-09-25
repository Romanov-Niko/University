package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.dao.jdbc.mapper.AudienceMapper;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.exception.EntityNotDeletedException;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotSavedException;
import com.foxminded.university.exception.EntityNotUpdatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcAudienceDao implements AudienceDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcAudienceDao.class);

    private static final String SQL_GET_AUDIENCE_BY_ID = "SELECT * FROM audiences WHERE id = ?";
    private static final String SQL_GET_ALL_AUDIENCES = "SELECT * FROM audiences";
    private static final String SQL_SAVE_AUDIENCE = "INSERT INTO audiences VALUES (DEFAULT, ?, ?)";
    private static final String SQL_UPDATE_AUDIENCE = "UPDATE audiences SET room_number = ?, capacity = ? WHERE id = ?";
    private static final String SQL_DELETE_AUDIENCE = "DELETE FROM audiences WHERE id = ?";
    private static final String SQL_AUDIENCE_BY_ROOM_NUMBER = "SELECT * FROM audiences WHERE room_number = ?";

    private final JdbcTemplate jdbcTemplate;
    private final AudienceMapper audienceMapper;

    public JdbcAudienceDao(JdbcTemplate jdbcTemplate, AudienceMapper audienceMapper) {
        this.audienceMapper = audienceMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Audience> getById(int id) {
        logger.debug("Retrieving audience with id {}", id);
        try {
            Optional<Audience> audience = Optional.of(jdbcTemplate.queryForObject(SQL_GET_AUDIENCE_BY_ID, audienceMapper, id));
            logger.debug("Audience was retrieved");
            return audience;
        } catch (EmptyResultDataAccessException exception) {
            logger.error("Audience is not present");
            return Optional.empty();
        }
    }

    @Override
    public List<Audience> getAll() {
        logger.debug("Retrieved all audiences");
        return jdbcTemplate.query(SQL_GET_ALL_AUDIENCES, audienceMapper);
    }

    @Override
    public void save(Audience audience) {
        logger.debug("Saving audience");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE_AUDIENCE, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, audience.getRoomNumber());
            statement.setInt(2, audience.getCapacity());
            return statement;
        }, keyHolder) == 0) {
            throw new EntityNotSavedException("Audience was not saved");
        } else {
            audience.setId((int) keyHolder.getKeys().get("id"));
            logger.debug("Audience was saved");
        }
    }

    @Override
    public void update(Audience audience) {
        logger.debug("Updating audience with id {}", audience.getId());
        if (jdbcTemplate.update(SQL_UPDATE_AUDIENCE, audience.getRoomNumber(), audience.getCapacity(), audience.getId()) == 0) {
            throw new EntityNotUpdatedException("Audience was not updated");
        } else {
            logger.debug("Audience was updated");
        }
    }

    @Override
    public void delete(int id) {
        logger.debug("Deleting audience with id {}", id);
        if (jdbcTemplate.update(SQL_DELETE_AUDIENCE, id) == 0) {
            throw new EntityNotDeletedException("Audience was not deleted");
        } else {
            logger.debug("Audience was deleted");
        }
    }

    @Override
    public Optional<Audience> getByRoomNumber(int roomNumber) {
        logger.debug("Retrieving audience with room number {}", roomNumber);
        try {
            Optional<Audience> audience = Optional.of(jdbcTemplate.queryForObject(SQL_AUDIENCE_BY_ROOM_NUMBER, audienceMapper, roomNumber));
            logger.debug("Audience was retrieved");
            return audience;
        } catch (EmptyResultDataAccessException exception) {
            logger.error("Audience is not present");
            return Optional.empty();
        }
    }
}
