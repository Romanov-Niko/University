package com.foxminded.university.domain;

import org.springframework.stereotype.Component;

import java.util.Objects;

public class Subject {

    private int id;
    private String name;
    private Integer creditHours;
    private Integer course;
    private String specialty;

    public Subject() {
    }

    public Subject(String name, Integer creditHours, Integer course, String specialty) {
        this.name = name;
        this.creditHours = creditHours;
        this.course = course;
        this.specialty = specialty;
    }

    public Subject(int id, String name, Integer creditHours, Integer course, String specialty) {
        this(name, creditHours, course, specialty);
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

    public Integer getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(Integer creditHours) {
        this.creditHours = creditHours;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return id == subject.id &&
                name.equals(subject.name) &&
                creditHours.equals(subject.creditHours) &&
                course.equals(subject.course) &&
                specialty.equals(subject.specialty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, creditHours, course, specialty);
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creditHours=" + creditHours +
                ", course=" + course +
                ", specialty='" + specialty + '\'' +
                '}';
    }
}
