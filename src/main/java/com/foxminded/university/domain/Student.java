package com.foxminded.university.domain;

import com.foxminded.university.validator.DateOrder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Objects;

@DateOrder
@Entity
@Table(name = "students")
public class Student extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "group_id")
    @Positive(message = "Group id must be positive")
    private Integer groupId;
    @NotBlank(message = "Specialty must not be blank")
    private String specialty;
    @NotNull(message = "Course must not be blank")
    @Positive(message = "Course number must be positive")
    private Integer course;
    @NotNull(message = "Admission date must not be blank")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate admission;
    @NotNull(message = "Graduation date must not be blank")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate graduation;

    public Student() {
    }

    public Student(String name, String surname, LocalDate dateOfBirth, String gender, String email,
                   String phoneNumber, Integer groupId, String specialty, Integer course, LocalDate admission, LocalDate graduation) {
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

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
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
                groupId.equals(student.groupId) &&
                specialty.equals(student.specialty) &&
                course.equals(student.course) &&
                admission.equals(student.admission) &&
                graduation.equals(student.graduation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, specialty, course, admission, graduation);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", specialty='" + specialty + '\'' +
                ", course=" + course +
                ", admission=" + admission +
                ", graduation=" + graduation +
                '}';
    }
}
