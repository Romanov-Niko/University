package com.foxminded.university.repository;

import com.foxminded.university.domain.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {

    List<Student> findAllByGroupId(int id);

    @Query(value = "SELECT * FROM students " +
            "LEFT JOIN groups ON students.group_id = groups.id " +
            "WHERE groups.name = :groupName", nativeQuery = true)
    List<Student> findAllByGroupName(@Param("groupName") String name);
}
