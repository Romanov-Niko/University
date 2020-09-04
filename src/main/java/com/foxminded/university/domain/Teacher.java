package com.foxminded.university.domain;

import java.time.LocalDate;
import java.util.List;

public class Teacher extends Person {

    private List<Subject> subjects;

    public Teacher(String name, String surname) {
        super(name, surname);
    }

    public Teacher(String name, String surname, LocalDate dateOfBirth, String gender, String email, String phoneNumber, List<Subject> subjects) {
        super(name, surname, dateOfBirth, gender, email, phoneNumber);
        this.subjects = subjects;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
