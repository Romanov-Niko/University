package com.foxminded.university.dao;

import com.foxminded.university.domain.Group;
import org.springframework.stereotype.Component;

import java.util.List;

public interface GroupDao extends Dao<Group> {

    @Override
    Group getById(int id);

    @Override
    List<Group> getAll();

    @Override
    void save(Group group);

    @Override
    void update(Group group);

    @Override
    void delete(int id);
}
