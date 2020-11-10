package com.foxminded.university.validation;

import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Person;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static com.foxminded.university.TestData.retrievedGroup;
import static com.foxminded.university.TestData.retrievedStudent;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonValidationTest {

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
    void givenCorrectPerson_whenPersonIsCreated_thenShouldHaveNoViolations() {
        Set<ConstraintViolation<Person>> violations = validator.validate(new Person("some", "person",
                LocalDate.parse("2000-08-02"), "male", "great@gmai.com", "0123456789"));

        assertTrue(violations.isEmpty());
    }

    @Test
    void givenInvalidName_whenPersonIsCreated_thenShouldDetectInvalidName() {
        Set<ConstraintViolation<Person>> violations = validator.validate(new Person("", "person",
                LocalDate.parse("2000-08-02"), "male", "great@gmai.com", "0123456789"));

        assertEquals(1, violations.size());

        ConstraintViolation<Person> violation = violations.iterator().next();
        assertEquals("Name must not be blank", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    void givenInvalidSurname_whenPersonIsCreated_thenShouldDetectInvalidSurname() {
        Set<ConstraintViolation<Person>> violations = validator.validate(new Person("some", "",
                LocalDate.parse("2000-08-02"), "male", "great@gmai.com", "0123456789"));

        assertEquals(1, violations.size());

        ConstraintViolation<Person> violation = violations.iterator().next();
        assertEquals("Surname must not be blank", violation.getMessage());
        assertEquals("surname", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    void givenInvalidDateOfBirth_whenPersonIsCreated_thenShouldDetectInvalidDateOfBirth() {
        Set<ConstraintViolation<Person>> violations = validator.validate(new Person("some", "person",
                LocalDate.parse("2022-08-02"), "male", "great@gmai.com", "0123456789"));

        assertEquals(1, violations.size());

        ConstraintViolation<Person> violation = violations.iterator().next();
        assertEquals("Date of birth must be in the past", violation.getMessage());
        assertEquals("dateOfBirth", violation.getPropertyPath().toString());
        assertEquals(LocalDate.parse("2022-08-02"), violation.getInvalidValue());
    }

    @Test
    void givenInvalidGender_whenPersonIsCreated_thenShouldDetectInvalidGender() {
        Set<ConstraintViolation<Person>> violations = validator.validate(new Person("some", "person",
                LocalDate.parse("2000-08-02"), "wrong", "great@gmai.com", "0123456789"));

        assertEquals(1, violations.size());

        ConstraintViolation<Person> violation = violations.iterator().next();
        assertEquals("Gender must be male or female", violation.getMessage());
        assertEquals("gender", violation.getPropertyPath().toString());
        assertEquals("wrong", violation.getInvalidValue());
    }

    @Test
    void givenInvalidMail_whenPersonIsCreated_thenShouldDetectInvalidMail() {
        Set<ConstraintViolation<Person>> violations = validator.validate(new Person("some", "person",
                LocalDate.parse("2000-08-02"), "male", "wrong@.com", "0123456789"));

        assertEquals(1, violations.size());

        ConstraintViolation<Person> violation = violations.iterator().next();
        assertEquals("Email must be valid", violation.getMessage());
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals("wrong@.com", violation.getInvalidValue());
    }

    @Test
    void givenInvalidPhoneNumber_whenPersonIsCreated_thenShouldDetectInvalidPhoneNumber() {
        Set<ConstraintViolation<Person>> violations = validator.validate(new Person("some", "person",
                LocalDate.parse("2000-08-02"), "male", "great@gmail.com", "55555"));

        assertEquals(1, violations.size());

        ConstraintViolation<Person> violation = violations.iterator().next();
        assertEquals("The phone number must be ten digits long", violation.getMessage());
        assertEquals("phoneNumber", violation.getPropertyPath().toString());
        assertEquals("55555", violation.getInvalidValue());
    }
}
