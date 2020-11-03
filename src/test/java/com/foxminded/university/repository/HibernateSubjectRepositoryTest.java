package com.foxminded.university.repository;

import com.foxminded.university.domain.Subject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class HibernateSubjectRepositoryTest {

    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    void givenId1_whenFindAllByTeacherId_thenReturnedAllSubjectsOfFirstTeacher() {
        List<Subject> expectedSubjects = singletonList(retrievedSubject);

        List<Subject> actualSubjects = subjectRepository.findAllByTeacherId(1);

        assertEquals(expectedSubjects, actualSubjects);
    }

    @Test
    void givenNameOfFirstSubject_whenFindByName_thenReturnedFirstSubject() {
        Subject actualSubject = subjectRepository.findByName("Calculus").orElse(null);

        assertEquals(retrievedSubject, actualSubject);
    }

    @Test
    void givenNonExistentTeacherId_whenFindAllByTeacherId_thenReturnedEmptyList() {
        List<Subject> actualSubjects = subjectRepository.findAllByTeacherId(0);

        assertEquals(emptyList(), actualSubjects);
    }

    @Test
    void givenNonExistentName_whenFindByName_thenReturnedOptionalEmpty() {
        Optional<Subject> actualSubject = subjectRepository.findByName("INCORRECT");

        assertEquals(Optional.empty(), actualSubject);
    }
}