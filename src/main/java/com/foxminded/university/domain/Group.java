package com.foxminded.university.domain;

import java.util.List;

public class Group {

    private String name;
    private List<Student> students;

    public Group(String name) {
        this.name = name;
    }

    public Group(String name, List<Student> students) {
        this(name);
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void removeStudent(int studentId) {
        this.students.remove(studentId);
    }
}
