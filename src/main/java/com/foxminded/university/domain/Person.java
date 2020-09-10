package com.foxminded.university.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Person {

    private int id;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String gender;
    private String email;
    private String phoneNumber;

    public Person() {
    }

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Person(String name, String surname, LocalDate dateOfBirth, String gender, String email, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Person(int id, String name, String surname, LocalDate dateOfBirth, String gender, String email, String phoneNumber) {
        this(name, surname, dateOfBirth, gender, email, phoneNumber);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return id == person.id &&
                name.equals(person.name) &&
                surname.equals(person.surname) &&
                dateOfBirth.equals(person.dateOfBirth) &&
                gender.equals(person.gender) &&
                email.equals(person.email) &&
                phoneNumber.equals(person.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, dateOfBirth, gender, email, phoneNumber);
    }
}
