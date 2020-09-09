package com.foxminded.university.domain;

import java.time.LocalDate;

public class Student extends Person {

    private int id;
    private int personId;
    private Group group;
    private String specialty;
    private Integer course;
    private LocalDate admission;
    private LocalDate graduation;

    public Student() {
    }

    public Student(String name, String surname, Group group) {
        super(name, surname);
        this.group = group;
    }

    public Student(String name, String surname, LocalDate dateOfBirth, String gender, String email,
                   String phoneNumber, Group group, String specialty, Integer course, LocalDate admission, LocalDate graduation) {
        super(name, surname, dateOfBirth, gender, email, phoneNumber);
        this.group = group;
        this.specialty = specialty;
        this.course = course;
        this.admission = admission;
        this.graduation = graduation;
    }



    public Student(int id, String name, String surname, LocalDate dateOfBirth, String gender, String email, String phoneNumber,
                   Group group, String specialty, Integer course, LocalDate admission, LocalDate graduation) {
        super(id, name, surname, dateOfBirth, gender, email, phoneNumber);
        this.personId = id;
        this.group = group;
        this.specialty = specialty;
        this.course = course;
        this.admission = admission;
        this.graduation = graduation;
    }

    public Student(int personId, String name, String surname, LocalDate dateOfBirth, String gender, String email, String phoneNumber,
                   int studentId, Group group, String specialty, Integer course, LocalDate admission, LocalDate graduation) {
        super(personId, name, surname, dateOfBirth, gender, email, phoneNumber);
        this.id = studentId;
        this.personId = personId;
        this.group = group;
        this.specialty = specialty;
        this.course = course;
        this.admission = admission;
        this.graduation = graduation;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
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
}
