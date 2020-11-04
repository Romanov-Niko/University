package com.foxminded.university.repository;

import com.foxminded.university.domain.LessonTime;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LessonTimeRepository extends CrudRepository<LessonTime, Integer> {

    List<LessonTime> findAll();

    Optional<LessonTime> findByBeginAndEnd(LocalTime start, LocalTime end);
}
