package com.foxminded.university.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class DaySchedule {

    private int id;
    private LocalDate day;
    private List<Lesson> lessons;

    public DaySchedule() {
    }

    public DaySchedule(int id, LocalDate day) {
        this.id = id;
        this.day = day;
    }

    public DaySchedule(LocalDate day, List<Lesson> lessons) {
        this.day = day;
        this.lessons = lessons;
    }

    public DaySchedule(int id, LocalDate day, List<Lesson> lessons) {
        this(day, lessons);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DaySchedule that = (DaySchedule) o;
        return id == that.id &&
                day.equals(that.day) &&
                lessons.equals(that.lessons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, day, lessons);
    }
}
