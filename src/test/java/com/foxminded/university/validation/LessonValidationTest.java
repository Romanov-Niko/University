package com.foxminded.university.validation;

import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Lesson;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

class LessonValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    void givenCorrectLesson_whenLessonIsCreated_thenShouldHaveNoViolations() {
        Set<ConstraintViolation<Lesson>> violations = validator.validate(retrievedLesson);

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenInvalidSubject_whenLessonIsCreated_thenShouldDetectInvalidSubject() {
        Set<ConstraintViolation<Lesson>> violations = validator.validate(new Lesson(1, null, retrievedTeacher,
                singletonList(retrievedGroup), retrievedAudience, retrievedLessonTime, LocalDate.parse("2017-06-01")));

        assertEquals(1, violations.size());

        ConstraintViolation<Lesson> violation = violations.iterator().next();
        assertEquals("Must have subject", violation.getMessage());
        assertEquals("subject", violation.getPropertyPath().toString());
        assertNull(violation.getInvalidValue());
    }

    @Test
    void givenInvalidTeacher_whenLessonIsCreated_thenShouldDetectInvalidTeacher() {
        Set<ConstraintViolation<Lesson>> violations = validator.validate(new Lesson(1, retrievedSubject, null,
                singletonList(retrievedGroup), retrievedAudience, retrievedLessonTime, LocalDate.parse("2017-06-01")));

        assertEquals(1, violations.size());

        ConstraintViolation<Lesson> violation = violations.iterator().next();
        assertEquals("Must have teacher", violation.getMessage());
        assertEquals("teacher", violation.getPropertyPath().toString());
        assertNull(violation.getInvalidValue());
    }

    @Test
    void givenInvalidGroups_whenLessonIsCreated_thenShouldDetectInvalidGroups() {
        Set<ConstraintViolation<Lesson>> violations = validator.validate(new Lesson(1, retrievedSubject, retrievedTeacher,
                emptyList(), retrievedAudience, retrievedLessonTime, LocalDate.parse("2017-06-01")));

        assertEquals(1, violations.size());

        ConstraintViolation<Lesson> violation = violations.iterator().next();
        assertEquals("Must have groups", violation.getMessage());
        assertEquals("groups", violation.getPropertyPath().toString());
        assertEquals(emptyList(), violation.getInvalidValue());
    }

    @Test
    void givenInvalidAudience_whenLessonIsCreated_thenShouldDetectInvalidAudience() {
        Set<ConstraintViolation<Lesson>> violations = validator.validate(new Lesson(1, retrievedSubject, retrievedTeacher,
                singletonList(retrievedGroup), null, retrievedLessonTime, LocalDate.parse("2017-06-01")));

        assertEquals(1, violations.size());

        ConstraintViolation<Lesson> violation = violations.iterator().next();
        assertEquals("Must have audience", violation.getMessage());
        assertEquals("audience", violation.getPropertyPath().toString());
        assertNull(violation.getInvalidValue());
    }

    @Test
    void givenInvalidLessonTime_whenLessonIsCreated_thenShouldDetectInvalidLessonTime() {
        Set<ConstraintViolation<Lesson>> violations = validator.validate(new Lesson(1, retrievedSubject, retrievedTeacher,
                singletonList(retrievedGroup), retrievedAudience, null, LocalDate.parse("2017-06-01")));

        assertEquals(1, violations.size());

        ConstraintViolation<Lesson> violation = violations.iterator().next();
        assertEquals("Must not be blank", violation.getMessage());
        assertEquals("lessonTime", violation.getPropertyPath().toString());
        assertNull(violation.getInvalidValue());
    }

    @Test
    void givenInvalidDate_whenLessonIsCreated_thenShouldDetectInvalidDate() {
        Set<ConstraintViolation<Lesson>> violations = validator.validate(new Lesson(1, retrievedSubject, retrievedTeacher,
                singletonList(retrievedGroup), retrievedAudience, retrievedLessonTime, null));

        assertEquals(1, violations.size());

        ConstraintViolation<Lesson> violation = violations.iterator().next();
        assertEquals("Must not be blank", violation.getMessage());
        assertEquals("date", violation.getPropertyPath().toString());
        assertNull(violation.getInvalidValue());
    }
}
