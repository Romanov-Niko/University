package com.foxminded.university.dao.mapper;

import com.foxminded.university.domain.Audience;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AudienceMapper implements RowMapper<Audience> {

    @Override
    public Audience mapRow(ResultSet resultSet, int i) throws SQLException {
        Audience audience = new Audience();
        audience.setId(resultSet.getInt("id"));
        audience.setRoomNumber(resultSet.getInt("room_number"));
        audience.setCapacity(resultSet.getInt("capacity"));
        return audience;
    }
}
