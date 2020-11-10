package com.foxminded.university.validation;

import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.Student;
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
import static com.foxminded.university.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

class StudentValidationTest {

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
    void givenCorrectStudent_whenStudentIsCreated_thenShouldHaveNoViolations() {
        Set<ConstraintViolation<Student>> violations = validator.validate(new Student(1, "first",
                "student", LocalDate.parse("1990-01-01"), "male", "first@gmail.com", "0123456789",
                1, "math", 1, LocalDate.parse("2015-06-01"), LocalDate.parse("2020-06-01")));

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenInvalidGroupId_whenStudentIsCreated_thenShouldDetectInvalidGroupId() {
        Set<ConstraintViolation<Student>> violations = validator.validate(new Student(1, "first",
                "student", LocalDate.parse("1990-01-01"), "male", "first@gmail.com", "0123456789",
                -5, "math", 1, LocalDate.parse("2015-06-01"), LocalDate.parse("2020-06-01")));

        assertEquals(1, violations.size());

        ConstraintViolation<Student> violation = violations.iterator().next();
        assertEquals("Group id must be positive", violation.getMessage());
        assertEquals("groupId", violation.getPropertyPath().toString());
        assertEquals(-5, violation.getInvalidValue());
    }

    @Test
    void givenInvalidSpecialty_whenStudentIsCreated_thenShouldDetectInvalidSpecialty() {
        Set<ConstraintViolation<Student>> violations = validator.validate(new Student(1, "first",
                "student", LocalDate.parse("1990-01-01"), "male", "first@gmail.com", "0123456789",
                1, "", 1, LocalDate.parse("2015-06-01"), LocalDate.parse("2020-06-01")));

        assertEquals(1, violations.size());

        ConstraintViolation<Student> violation = violations.iterator().next();
        assertEquals("Specialty must not be blank", violation.getMessage());
        assertEquals("specialty", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    void givenInvalidCourse_whenStudentIsCreated_thenShouldDetectInvalidCourse() {
        Set<ConstraintViolation<Student>> violations = validator.validate(new Student(1, "first",
                "student", LocalDate.parse("1990-01-01"), "male", "first@gmail.com", "0123456789",
                1, "math", -3, LocalDate.parse("2015-06-01"), LocalDate.parse("2020-06-01")));

        assertEquals(1, violations.size());

        ConstraintViolation<Student> violation = violations.iterator().next();
        assertEquals("Course number must be positive", violation.getMessage());
        assertEquals("course", violation.getPropertyPath().toString());
        assertEquals(-3, violation.getInvalidValue());
    }

    @Test
    void givenInvalidAdmission_whenStudentIsCreated_thenShouldDetectInvalidAdmission() {
        Set<ConstraintViolation<Student>> violations = validator.validate(new Student(1, "first",
                "student", LocalDate.parse("1990-01-01"), "male", "first@gmail.com", "0123456789",
                1, "math", 4,null, LocalDate.parse("2020-06-01")));

        assertEquals(1, violations.size());

        ConstraintViolation<Student> violation = violations.iterator().next();
        assertEquals("Admission date must not be blank", violation.getMessage());
        assertEquals("admission", violation.getPropertyPath().toString());
        assertNull(violation.getInvalidValue());
    }

    @Test
    void givenInvalidGraduation_whenStudentIsCreated_thenShouldDetectInvalidGraduation() {
        Set<ConstraintViolation<Student>> violations = validator.validate(new Student(1, "first",
                "student", LocalDate.parse("1990-01-01"), "male", "first@gmail.com", "0123456789",
                1, "math", 4, LocalDate.parse("2015-06-01"), null));

        assertEquals(1, violations.size());

        ConstraintViolation<Student> violation = violations.iterator().next();
        assertEquals("Graduation date must not be blank", violation.getMessage());
        assertEquals("graduation", violation.getPropertyPath().toString());
        assertNull(violation.getInvalidValue());
    }
}
