package com.foxminded.university.domain;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "lessons_times")
public class LessonTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "begin_time")
    private LocalTime begin;
    @Column(name = "end_time")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonTime that = (LessonTime) o;
        return id == that.id &&
                begin.equals(that.begin) &&
                end.equals(that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, begin, end);
    }

    @Override
    public String toString() {
        return "LessonTime{" +
                "id=" + id +
                ", begin=" + begin +
                ", end=" + end +
                '}';
    }
}
