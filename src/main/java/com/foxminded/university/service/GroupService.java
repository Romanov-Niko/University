package com.foxminded.university.service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.domain.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupService implements Service<Group>{

    private final GroupDao groupDao;

    @Autowired
    public GroupService (GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    public Group getById(int id) {
        return groupDao.getById(id);
    }

    @Override
    public List<Group> getAll() {
        return groupDao.getAll();
    }

    @Override
    public void save(Group group) {
        groupDao.save(group);
    }

    @Override
    public void update(Group group) {
        groupDao.update(group);
    }

    @Override
    public void delete(int id) {
        groupDao.delete(id);
    }

    public List<Group> getAllByLessonId(int id) {
        return groupDao.getAllByLessonId(id);
    }
}
