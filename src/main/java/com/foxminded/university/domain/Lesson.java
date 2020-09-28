package com.foxminded.university.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Lesson {

    private int id;
    private Subject subject;
    private Teacher teacher;
    private List<Group> groups;
    private Audience audience;
    private LessonTime lessonTime;
    private LocalDate date;

    public Lesson() {
    }

    public Lesson(Subject subject, Teacher teacher, List<Group> groups, Audience audience, LessonTime lessonTime, LocalDate date) {
        this.subject = subject;
        this.teacher = teacher;
        this.groups = groups;
        this.audience = audience;
        this.lessonTime = lessonTime;
        this.date = date;
    }

    public Lesson(int id, Subject subject, Teacher teacher, List<Group> groups, Audience audience, LessonTime lessonTime, LocalDate date) {
        this(subject, teacher, groups, audience, lessonTime, date);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    public LessonTime getLessonTime() {
        return lessonTime;
    }

    public void setLessonTime(LessonTime lessonTime) {
        this.lessonTime = lessonTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return id == lesson.id &&
                subject.equals(lesson.subject) &&
                teacher.equals(lesson.teacher) &&
                groups.equals(lesson.groups) &&
                audience.equals(lesson.audience) &&
                lessonTime.equals(lesson.lessonTime) &&
                date.equals(lesson.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, teacher, groups, audience, lessonTime, date);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", subject=" + subject +
                ", teacher=" + teacher +
                ", groups=" + groups +
                ", audience=" + audience +
                ", lessonTime=" + lessonTime +
                ", date=" + date +
                '}';
    }
}
