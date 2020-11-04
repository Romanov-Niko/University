package com.foxminded.university.repository;

import com.foxminded.university.domain.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends CrudRepository<Group, Integer> {

    List<Group> findAll();

    Optional<Group> findByName(String name);
}
