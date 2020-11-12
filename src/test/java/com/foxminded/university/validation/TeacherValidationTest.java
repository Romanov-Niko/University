package com.foxminded.university.validation;

import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.Teacher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static com.foxminded.university.TestData.retrievedAudience;
import static com.foxminded.university.TestData.retrievedSubject;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TeacherValidationTest {

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
    void givenCorrectTeacher_whenTeacherIsCreated_thenShouldHaveNoViolations() {
        Set<ConstraintViolation<Teacher>> violations = validator.validate(new Teacher(1, "first", "teacher",
                LocalDate.parse("1990-01-01"), "male", "first@gmail.com", "0123456789", singletonList(retrievedSubject)));

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenInvalidSubjects_whenTeacherIsCreated_thenShouldDetectInvalidSubjects() {
        Set<ConstraintViolation<Teacher>> violations = validator.validate(new Teacher(1, "first", "teacher",
                LocalDate.parse("1990-01-01"), "male", "first@gmail.com", "0123456789", emptyList()));

        assertEquals(1, violations.size());

        ConstraintViolation<Teacher> violation = violations.iterator().next();
        assertEquals("Must have subjects", violation.getMessage());
        assertEquals("subjects", violation.getPropertyPath().toString());
        assertEquals( emptyList(), violation.getInvalidValue());
    }
}
