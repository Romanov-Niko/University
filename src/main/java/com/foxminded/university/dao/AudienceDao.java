package com.foxminded.university.dao;

import com.foxminded.university.domain.Audience;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

public interface AudienceDao extends Dao<Audience> {

    Optional<Audience> getByRoomNumber(int roomNumber);
}
