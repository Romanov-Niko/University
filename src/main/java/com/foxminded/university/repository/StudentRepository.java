package com.foxminded.university.repository;

import com.foxminded.university.domain.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {

    List<Student> findAll();

    List<Student> findAllByGroupId(int id);
}
