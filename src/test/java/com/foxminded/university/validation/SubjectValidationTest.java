package com.foxminded.university.validation;

import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Subject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubjectValidationTest {

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
    void givenCorrectSubject_whenSubjectIsCreated_thenShouldHaveNoViolations() {
        Set<ConstraintViolation<Subject>> violations = validator.validate(retrievedSubject);

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenInvalidName_whenSubjectIsCreated_thenShouldDetectInvalidName() {
        Set<ConstraintViolation<Subject>> violations = validator.validate(new Subject(1, "", 120, 1, "math"));

        assertEquals(1, violations.size());

        ConstraintViolation<Subject> violation = violations.iterator().next();
        assertEquals("Subject name must not be blank", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    void givenInvalidCreditHoursAmount_whenSubjectIsCreated_thenShouldDetectInvalidCreditHoursAmount() {
        Set<ConstraintViolation<Subject>> violations = validator.validate(new Subject(1, "Calculus", -20, 1, "math"));

        assertEquals(1, violations.size());

        ConstraintViolation<Subject> violation = violations.iterator().next();
        assertEquals("Must be positive", violation.getMessage());
        assertEquals("creditHours", violation.getPropertyPath().toString());
        assertEquals(-20, violation.getInvalidValue());
    }

    @Test
    void givenInvalidCourseNumber_whenSubjectIsCreated_thenShouldDetectInvalidCourseNumber() {
        Set<ConstraintViolation<Subject>> violations = validator.validate(new Subject(1, "Calculus", 120, -8, "math"));

        assertEquals(1, violations.size());

        ConstraintViolation<Subject> violation = violations.iterator().next();
        assertEquals("Must be positive", violation.getMessage());
        assertEquals("course", violation.getPropertyPath().toString());
        assertEquals(-8, violation.getInvalidValue());
    }

    @Test
    void givenInvalidSpecialty_whenSubjectIsCreated_thenShouldDetectInvalidSpecialty() {
        Set<ConstraintViolation<Subject>> violations = validator.validate(new Subject(1, "Calculus", 120, 1, ""));

        assertEquals(1, violations.size());

        ConstraintViolation<Subject> violation = violations.iterator().next();
        assertEquals("Specialty must not be blank", violation.getMessage());
        assertEquals("specialty", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }
}
