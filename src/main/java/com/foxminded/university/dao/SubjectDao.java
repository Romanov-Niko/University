package com.foxminded.university.dao;

import com.foxminded.university.domain.Subject;
import org.springframework.stereotype.Component;

import java.util.List;

public interface SubjectDao extends Dao<Subject> {

    List<Subject> getAllByTeacherId(int id);
}
