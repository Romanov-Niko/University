package com.foxminded.university.domain;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teachers")
public class Teacher extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(min = 1)
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
            name = "teachers_subjects",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects;

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
