package com.foxminded.university.dao;

import com.foxminded.university.domain.Group;

import java.util.List;

public interface GroupDao extends Dao<Group> {

    Group getById(int id);

    List<Group> getAll();

    void save(Group group);

    void update(Group group);

    void delete(int id);

    List<Group> getAllByLessonId(int id);
}
