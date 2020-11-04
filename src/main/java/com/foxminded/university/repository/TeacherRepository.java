package com.foxminded.university.repository;

import com.foxminded.university.domain.Teacher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends CrudRepository<Teacher, Integer> {

    List<Teacher> findAll();
}
