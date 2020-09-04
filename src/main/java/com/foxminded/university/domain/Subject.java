package com.foxminded.university.domain;

public class Subject {

    private String name;
    private Integer creditHours;
    private Integer course;
    private String specialty;

    public Subject(String name, Integer creditHours, Integer course, String specialty) {
        this.name = name;
        this.creditHours = creditHours;
        this.course = course;
        this.specialty = specialty;
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
}
