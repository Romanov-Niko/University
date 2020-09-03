package com.foxminded.university.domain;

import java.util.List;

public class Group {

    private Integer groupId;
    private String name;
    private List<Student> students;

    public Group(String name) {
        this.name = name;
    }

    public Group(String name, List<Student> students) {
        this(name);
        this.students = students;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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
