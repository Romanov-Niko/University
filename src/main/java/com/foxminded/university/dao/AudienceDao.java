package com.foxminded.university.dao;

import com.foxminded.university.domain.Audience;
import org.springframework.stereotype.Component;

import java.util.List;

public interface AudienceDao extends Dao<Audience> {

    @Override
    Audience getById(int id);

    @Override
    List<Audience> getAll();

    @Override
    void save(Audience audience);

    @Override
    void update(Audience audience);

    @Override
    void delete(int id);
}
