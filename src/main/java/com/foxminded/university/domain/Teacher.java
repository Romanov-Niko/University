package com.foxminded.university.domain;

import java.time.LocalDate;
import java.util.List;

public class Teacher extends Person {

    private int id;
    private int personId;
    private List<Subject> subjects;

    public Teacher() {
    }

    public Teacher(String name, String surname) {
        super(name, surname);
    }

    public Teacher(String name, String surname, LocalDate dateOfBirth, String gender, String email, String phoneNumber, List<Subject> subjects) {
        super(name, surname, dateOfBirth, gender, email, phoneNumber);
        this.subjects = subjects;
    }

    public Teacher(int id, String name, String surname, LocalDate dateOfBirth, String gender, String email, String phoneNumber, List<Subject> subjects) {
        super(id, name, surname, dateOfBirth, gender, email, phoneNumber);
        this.personId = id;
        this.subjects = subjects;
    }

    public Teacher(int personId, String name, String surname, LocalDate dateOfBirth, String gender, String email, String phoneNumber, int teacherId, List<Subject> subjects) {
        super(personId, name, surname, dateOfBirth, gender, email, phoneNumber);
        this.id = teacherId;
        this.personId = personId;
        this.subjects = subjects;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
