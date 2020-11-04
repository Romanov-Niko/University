package com.foxminded.university.repository;

import com.foxminded.university.domain.Subject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Integer> {

    List<Subject> findAll();

    Optional<Subject> findByName(String name);
}
