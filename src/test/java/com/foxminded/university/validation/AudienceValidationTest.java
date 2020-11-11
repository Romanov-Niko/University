package com.foxminded.university.validation;

import com.foxminded.university.domain.Audience;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import static com.foxminded.university.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

class AudienceValidationTest {

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
    void givenCorrectAudience_whenAudienceIsCreated_thenShouldHaveNoViolations() {
        Set<ConstraintViolation<Audience>> violations = validator.validate(retrievedAudience);

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenInvalidRoomNumber_whenAudienceIsCreated_thenShouldDetectInvalidRoomNumber() {
        Set<ConstraintViolation<Audience>> violations = validator.validate(new Audience(1, -3, 4));

        assertEquals(1, violations.size());

        ConstraintViolation<Audience> violation = violations.iterator().next();
        assertEquals("Must be positive", violation.getMessage());
        assertEquals("roomNumber", violation.getPropertyPath().toString());
        assertEquals(-3, violation.getInvalidValue());
    }

    @Test
    void givenInvalidCapacity_whenAudienceIsCreated_thenShouldDetectInvalidCapacity() {
        Set<ConstraintViolation<Audience>> violations = validator.validate(new Audience(1, 1, -8));

        assertEquals(1, violations.size());

        ConstraintViolation<Audience> violation = violations.iterator().next();
        assertEquals("Must be positive", violation.getMessage());
        assertEquals("capacity", violation.getPropertyPath().toString());
        assertEquals(-8, violation.getInvalidValue());
    }
}
