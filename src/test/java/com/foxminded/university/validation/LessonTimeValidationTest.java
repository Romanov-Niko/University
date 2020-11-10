package com.foxminded.university.validation;

import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.LessonTime;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

class LessonTimeValidationTest {

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
    void givenCorrectLessonTime_whenLessonTimeIsCreated_thenShouldHaveNoViolations() {
        Set<ConstraintViolation<LessonTime>> violations = validator.validate(retrievedLessonTime);

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenInvalidBeginTime_whenLessonTimeIsCreated_thenShouldDetectInvalidBeginTime() {
        Set<ConstraintViolation<LessonTime>> violations = validator.validate(new LessonTime(1, null, LocalTime.parse("09:00:00")));

        assertEquals(1, violations.size());

        ConstraintViolation<LessonTime> violation = violations.iterator().next();
        assertEquals("Begin time must not be blank", violation.getMessage());
        assertEquals("begin", violation.getPropertyPath().toString());
        assertNull(violation.getInvalidValue());
    }

    @Test
    void givenInvalidEndTime_whenLessonTimeIsCreated_thenShouldDetectInvalidEndTime() {
        Set<ConstraintViolation<LessonTime>> violations = validator.validate(new LessonTime(1, LocalTime.parse("08:00:00"), null));

        assertEquals(1, violations.size());

        ConstraintViolation<LessonTime> violation = violations.iterator().next();
        assertEquals("End time must not be blank", violation.getMessage());
        assertEquals("end", violation.getPropertyPath().toString());
        assertNull(violation.getInvalidValue());
    }

    @Test
    void givenIncorrectEndTimeWhichIsBeforeStartTime_whenLessonTimeIsCreated_thenShouldDetectInvalidTimeOrder() {
        LessonTime lessonTimeWithWrongTimeOrder = new LessonTime(1, LocalTime.parse("09:00:00"), LocalTime.parse("08:00:00"));
        Set<ConstraintViolation<LessonTime>> violations = validator.validate(lessonTimeWithWrongTimeOrder);

        assertEquals(2, violations.size());

        ConstraintViolation<LessonTime> beginViolation = violations.iterator().next();
        assertEquals("Begin can not be after end", beginViolation.getMessage());
        assertEquals("begin", beginViolation.getPropertyPath().toString());
        assertEquals(lessonTimeWithWrongTimeOrder, beginViolation.getInvalidValue());

        violations.remove(beginViolation);

        ConstraintViolation<LessonTime> endViolation = violations.iterator().next();
        assertEquals("End can not be before begin", endViolation.getMessage());
        assertEquals("end", endViolation.getPropertyPath().toString());
        assertEquals(lessonTimeWithWrongTimeOrder, endViolation.getInvalidValue());
    }
}
