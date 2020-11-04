package com.foxminded.university.repository;

import com.foxminded.university.domain.Subject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.foxminded.university.TestData.retrievedSubject;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class SubjectRepositoryTest {

    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    void givenNameOfFirstSubject_whenFindByName_thenReturnedFirstSubject() {
        Subject actualSubject = subjectRepository.findByName("Calculus").orElse(null);

        assertEquals(retrievedSubject, actualSubject);
    }

    @Test
    void givenNonExistentName_whenFindByName_thenReturnedOptionalEmpty() {
        Optional<Subject> actualSubject = subjectRepository.findByName("INCORRECT");

        assertEquals(Optional.empty(), actualSubject);
    }
}