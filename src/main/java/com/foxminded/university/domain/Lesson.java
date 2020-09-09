package com.foxminded.university.domain;

import java.util.List;

public class Lesson {

    private int id;
    private Subject subject;
    private Teacher teacher;
    private List<Group> groups;
    private Audience audience;
    private LessonTime lessonTime;

    public Lesson() {
    }

    public Lesson(Teacher teacher, List<Group> groups) {
        this.teacher = teacher;
        this.groups = groups;
    }

    public Lesson(Subject subject, Teacher teacher, List<Group> groups, Audience audience, LessonTime lessonTime) {
        this.subject = subject;
        this.teacher = teacher;
        this.groups = groups;
        this.audience = audience;
        this.lessonTime = lessonTime;
    }

    public Lesson(int id, Subject subject, Teacher teacher, List<Group> groups, Audience audience, LessonTime lessonTime) {
        this(subject, teacher, groups, audience, lessonTime);
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
}
