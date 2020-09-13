package com.foxminded.university.dao;

import com.foxminded.university.domain.Audience;

import java.util.List;

public interface AudienceDao extends Dao<Audience> {

    Audience getById(int id);

    List<Audience> getAll();

    void save(Audience audience);

    void update(Audience audience);

    void delete(int id);
}
