package com.foxminded.university.validation;

import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.Group;
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

class GroupValidationTest {

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
    void givenCorrectGroup_whenGroupIsCreated_thenShouldHaveNoViolations() {
        Set<ConstraintViolation<Group>> violations = validator.validate(retrievedGroup);

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenInvalidName_whenGroupIsCreated_thenShouldDetectInvalidName() {
        Set<ConstraintViolation<Group>> violations = validator.validate(new Group(1, "", singletonList(retrievedStudent)));

        assertEquals(1, violations.size());

        ConstraintViolation<Group> violation = violations.iterator().next();
        assertEquals("Must not be blank", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    void givenInvalidListOfStudents_whenGroupIsCreated_thenShouldDetectInvalidListOfStudents() {
        Set<ConstraintViolation<Group>> violations = validator.validate(new Group(1, "NEW", emptyList()));

        assertEquals(1, violations.size());

        ConstraintViolation<Group> violation = violations.iterator().next();
        assertEquals("Group must have students", violation.getMessage());
        assertEquals("students", violation.getPropertyPath().toString());
        assertEquals(emptyList(), violation.getInvalidValue());
    }
}
