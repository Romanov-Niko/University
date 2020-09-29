package com.foxminded.university.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class DaySchedule {

    private LocalDate day;
    private List<Lesson> lessons;

    public DaySchedule() {
    }

    public DaySchedule(LocalDate day, List<Lesson> lessons) {
        this.day = day;
        this.lessons = lessons;
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
        return day.equals(that.day) &&
                lessons.equals(that.lessons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, lessons);
    }

    @Override
    public String toString() {
        return "DaySchedule{" +
                "day=" + day +
                ", lessons=" + lessons +
                '}';
    }
}
