package com.foxminded.university.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Student extends Person {

    private int id;
    private int groupId;
    private String specialty;
    private Integer course;
    private LocalDate admission;
    private LocalDate graduation;

    public Student() {
    }

    public Student(String name, String surname, LocalDate dateOfBirth, String gender, String email,
                   String phoneNumber, int groupId, String specialty, Integer course, LocalDate admission, LocalDate graduation) {
        super(name, surname, dateOfBirth, gender, email, phoneNumber);
        this.groupId = groupId;
        this.specialty = specialty;
        this.course = course;
        this.admission = admission;
        this.graduation = graduation;
    }

    public Student(int id, String name, String surname, LocalDate dateOfBirth, String gender, String email, String phoneNumber,
                   int groupId, String specialty, Integer course, LocalDate admission, LocalDate graduation) {
        this(name, surname, dateOfBirth, gender, email, phoneNumber, groupId, specialty, course, admission, graduation);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
    }

    public LocalDate getAdmission() {
        return admission;
    }

    public void setAdmission(LocalDate admission) {
        this.admission = admission;
    }

    public LocalDate getGraduation() {
        return graduation;
    }

    public void setGraduation(LocalDate graduation) {
        this.graduation = graduation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id &&
                groupId == student.groupId &&
                specialty.equals(student.specialty) &&
                course.equals(student.course) &&
                admission.equals(student.admission) &&
                graduation.equals(student.graduation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, specialty, course, admission, graduation);
    }
}
