package com.foxminded.university.validation;

import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.LessonTime;
import com.foxminded.university.domain.Student;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

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
        assertEquals("Must be positive", violation.getMessage());
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
        assertEquals("Must not be blank", violation.getMessage());
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
        assertEquals("Must be positive", violation.getMessage());
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
        assertEquals("Must not be blank", violation.getMessage());
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
        assertEquals("Must not be blank", violation.getMessage());
        assertEquals("graduation", violation.getPropertyPath().toString());
        assertNull(violation.getInvalidValue());
    }

    @Test
    void givenIncorrectGraduationWhichIsBeforeAdmission_whenStudentIsCreated_thenShouldDetectInvalidDateOrder() {
        Student studentWithWrongDateOrder = new Student(1, "first",
                "student", LocalDate.parse("1990-01-01"), "male", "first@gmail.com", "0123456789",
                1, "math", 4,LocalDate.parse("2020-06-02"), LocalDate.parse("2020-06-01"));
        Set<ConstraintViolation<Student>> violations = validator.validate(studentWithWrongDateOrder);

        assertEquals(2, violations.size());

        ConstraintViolation<Student> beginViolation = violations.iterator().next();
        assertEquals("Graduation can not be before admission", beginViolation.getMessage());
        assertEquals("graduation", beginViolation.getPropertyPath().toString());
        assertEquals(studentWithWrongDateOrder, beginViolation.getInvalidValue());

        violations.remove(beginViolation);

        ConstraintViolation<Student> endViolation = violations.iterator().next();
        assertEquals("Admission can not be after graduation", endViolation.getMessage());
        assertEquals("admission", endViolation.getPropertyPath().toString());
        assertEquals(studentWithWrongDateOrder, endViolation.getInvalidValue());
    }

    @Test
    void givenEmptyPhoneNumber_whenStudentIsCreated_thenShouldHaveNoViolations() {
        Set<ConstraintViolation<Student>> violations = validator.validate(new Student(1, "first",
                "student", LocalDate.parse("1990-01-01"), "male", "first@gmail.com", "",
                1, "math", 4, LocalDate.parse("2015-06-01"), LocalDate.parse("2020-06-05")));

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenNullPhoneNumber_whenStudentIsCreated_thenShouldHaveNoViolations() {
        Set<ConstraintViolation<Student>> violations = validator.validate(new Student(1, "first",
                "student", LocalDate.parse("1990-01-01"), "male", "first@gmail.com", null,
                1, "math", 4, LocalDate.parse("2015-06-01"), LocalDate.parse("2020-06-05")));

        assertTrue(violations.isEmpty());
    }
}
