package com.foxminded.university.domain;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Must not be blank")
    private String name;
    @NotNull(message = "Must not be blank")
    @Positive(message = "Must be positive")
    @Column(name = "credit_hours")
    private Integer creditHours;
    @NotNull(message = "Must not be blank")
    @Positive(message = "Must be positive")
    private Integer course;
    @NotBlank(message = "Must not be blank")
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
