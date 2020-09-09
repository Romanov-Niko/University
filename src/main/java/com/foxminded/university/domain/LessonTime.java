package com.foxminded.university.domain;

import java.time.LocalTime;

public class LessonTime {

    private int id;
    private LocalTime begin;
    private LocalTime end;

    public LessonTime() {
    }

    public LessonTime(LocalTime begin, LocalTime end) {
        this.begin = begin;
        this.end = end;
    }

    public LessonTime(int id, LocalTime begin, LocalTime end) {
        this(begin, end);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalTime getBegin() {
        return begin;
    }

    public void setBegin(LocalTime begin) {
        this.begin = begin;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }
}
