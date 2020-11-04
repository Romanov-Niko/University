package com.foxminded.university.repository;

import com.foxminded.university.domain.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void givenId1_whenFindAllByGroupId_thenReturnedAllStudentsOfFirstGroup() {
        List<Student> actualStudents = studentRepository.findAllByGroupId(1);

        assertEquals(1, actualStudents.size());
    }

    @Test
    void givenFirstGroupName_whenFindAllByGroupName_thenReturnedAllStudentsOfFirstGroup() {
        List<Student> actualStudents = studentRepository.findAllByGroupName("AA-11");

        assertEquals(1, actualStudents.size());
    }

    @Test
    void givenNonExistentGroupId_whenFindAllByGroupId_thenReturnedEmptyList() {
        List<Student> actualStudents = studentRepository.findAllByGroupId(0);

        assertEquals(emptyList(), actualStudents);
    }

    @Test
    void givenNonExistentName_whenFindAllByGroupName_thenReturnedEmptyList() {
        List<Student> actualStudents = studentRepository.findAllByGroupName("INCORRECT");

        assertEquals(emptyList(), actualStudents);
    }
}