package com.foxminded.university.repository;

import com.foxminded.university.domain.Subject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Integer> {

    @Query(value = "SELECT teachers_subjects.teacher_id, subjects.id, subjects.name, " +
            "subjects.credit_hours, subjects.course, subjects.specialty " +
            "FROM teachers_subjects " +
            "LEFT JOIN teachers ON teachers_subjects.teacher_id = teachers.id " +
            "LEFT JOIN subjects ON teachers_subjects.subject_id = subjects.id " +
            "WHERE teachers.id = :teacherId", nativeQuery = true)
    List<Subject> findAllByTeacherId(@Param("teacherId") int id);

    Optional<Subject> findByName(String name);
}
