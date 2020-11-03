package com.foxminded.university.repository;

import com.foxminded.university.domain.Lesson;
import com.foxminded.university.repository.custom.LessonRepositoryCustom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, Integer>, LessonRepositoryCustom {

    List<Lesson> findAllByDate(LocalDate date);
}
