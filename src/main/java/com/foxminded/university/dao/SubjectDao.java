package com.foxminded.university.dao;

import com.foxminded.university.domain.Subject;

import java.util.List;

public interface SubjectDao extends Dao<Subject> {

    List<Subject> getAllByTeacherId(int id);
}
