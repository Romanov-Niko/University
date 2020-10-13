package com.foxminded.university.domain;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Teacher extends Person {

    private int id;
    private List<Subject> subjects = new ArrayList<>();

    public Teacher() {
    }

    public Teacher(String name, String surname, LocalDate dateOfBirth, String gender, String email, String phoneNumber, List<Subject> subjects) {
        super(name, surname, dateOfBirth, gender, email, phoneNumber);
        this.subjects = subjects;
    }

    public Teacher(int id, String name, String surname, LocalDate dateOfBirth, String gender, String email, String phoneNumber, List<Subject> subjects) {
        this(name, surname, dateOfBirth, gender, email, phoneNumber, subjects);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return id == teacher.id &&
                subjects.equals(teacher.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subjects);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", subjects=" + subjects +
                '}';
    }
}
