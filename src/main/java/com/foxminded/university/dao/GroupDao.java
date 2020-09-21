package com.foxminded.university.dao;

import com.foxminded.university.domain.Group;
import org.springframework.stereotype.Component;

import java.util.List;

public interface GroupDao extends Dao<Group> {

    List<Group> getAllByLessonId(int id);
}
