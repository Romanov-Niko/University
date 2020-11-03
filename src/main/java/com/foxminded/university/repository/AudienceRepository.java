package com.foxminded.university.repository;

import com.foxminded.university.domain.Audience;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AudienceRepository extends CrudRepository<Audience, Integer> {

    Optional<Audience> findByRoomNumber(int roomNumber);
}
