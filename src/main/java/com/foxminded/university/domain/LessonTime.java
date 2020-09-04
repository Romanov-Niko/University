package com.foxminded.university.domain;

import java.time.LocalTime;

public class LessonTime {

    private LocalTime begin;
    private LocalTime end;

    public LessonTime(LocalTime begin, LocalTime end) {
        this.begin = begin;
        this.end = end;
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