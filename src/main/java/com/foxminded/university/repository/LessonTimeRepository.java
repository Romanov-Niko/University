package com.foxminded.university.repository;

import com.foxminded.university.domain.LessonTime;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface LessonTimeRepository extends CrudRepository<LessonTime, Integer> {

    Optional<LessonTime> findByBeginAndEnd(LocalTime start, LocalTime end);
}
