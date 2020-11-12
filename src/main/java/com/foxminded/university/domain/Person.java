package com.foxminded.university.domain;

import com.foxminded.university.validator.TeacherValidationGroup;
import com.foxminded.university.validator.ValidPhoneNumber;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;


@MappedSuperclass
public class Person {

    @NotBlank(message = "Must not be blank")
    private String name;
    @NotBlank(message = "Must not be blank")
    private String surname;
    @Past(message = "Must be in the past")
    @NotNull(message = "Must not be blank")
    @Column(name = "date_of_birth")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;
    @NotBlank(message = "Must not be blank")
    @Pattern(regexp = "(([Ff]e)?[Mm]ale)?", message = "Must be male or female")
    private String gender;
    @NotBlank(message = "Must not be blank")
    @Email(message = "Must be valid")
    private String email;
    @NotBlank(message = "Must not be blank", groups = TeacherValidationGroup.class)
    @ValidPhoneNumber
    @Column(name = "phone_number")
    private String phoneNumber;

    public Person() {
    }

    public Person(String name, String surname, LocalDate dateOfBirth, String gender, String email, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return name.equals(person.name) &&
                surname.equals(person.surname) &&
                dateOfBirth.equals(person.dateOfBirth) &&
                gender.equals(person.gender) &&
                email.equals(person.email) &&
                phoneNumber.equals(person.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, dateOfBirth, gender, email, phoneNumber);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
