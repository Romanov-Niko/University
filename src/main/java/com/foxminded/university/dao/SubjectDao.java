package com.foxminded.university.dao;

import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Subject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

public interface SubjectDao extends Dao<Subject> {

    List<Subject> getAllByTeacherId(int id);

    Optional<Subject> getByName(String name);
}
