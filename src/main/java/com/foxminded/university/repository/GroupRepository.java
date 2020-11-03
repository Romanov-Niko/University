package com.foxminded.university.repository;

import com.foxminded.university.domain.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends CrudRepository<Group, Integer> {

    @Query(value = "SELECT lessons_groups.lesson_id, groups.id, groups.name " +
            "FROM lessons_groups " +
            "LEFT JOIN lessons ON lessons_groups.lesson_id = lessons.id " +
            "LEFT JOIN groups ON lessons_groups.group_id = groups.id " +
            "WHERE lessons.id = :lessonId", nativeQuery = true)
    List<Group> findAllByLessonId(@Param("lessonId") int id);

    Optional<Group> findByName(String name);
}
