package com.foxminded.university;

import com.foxminded.university.domain.*;
import org.junit.jupiter.api.Tag;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static java.util.Collections.singletonList;

public class EntitiesForTests {

    public static Audience audienceGetById = new Audience(1, 101, 100);
    public static Audience audienceSave = new Audience(104, 100);
    public static Audience audienceUpdate = new Audience(1, 999, 999);

    public static LessonTime lessonTimeGetById = new LessonTime(1, LocalTime.parse("08:00:00"),LocalTime.parse("09:00:00"));
    public static LessonTime lessonTimeSave = new LessonTime(LocalTime.parse("12:00:00"),LocalTime.parse("13:00:00"));
    public static LessonTime lessonTimeUpdate = new LessonTime(1, LocalTime.parse("22:00:00"),LocalTime.parse("23:00:00"));

    public static Person personGetById = new Person(1,"first", "teacher", LocalDate.parse("1990-01-01"),
            "male", "first@gmail.com", "11111");
    public static Person personSave = new Person(1,"first", "teacher", LocalDate.parse("1990-01-01"),
            "male", "first@gmail.com", "11111");
    public static Person personUpdate = new Person(1,"UPDATED", "TEACHER", LocalDate.parse("1990-01-01"),
            "male", "first@gmail.com", "11111");

    public static Student studentGetById = new Student(4, "first", "student", LocalDate.parse("1990-01-01"),
            "male", "first@gmail.com", "11111", 1, 1, "math",1,
            LocalDate.parse("2015-06-01"), LocalDate.parse("2020-06-01"));
    public static Student studentSave = new Student("new", "student", LocalDate.parse("1990-01-01"),
            "male", "first@gmail.com", "11111",1, "math",1,
            LocalDate.parse("2015-06-01"), LocalDate.parse("2020-06-01"));
    public static Student studentUpdate = new Student(4, "UPDATED", "STUDENT", LocalDate.parse("1990-01-01"),
            "male", "first@gmail.com", "11111", 1, 1, "math",1,
            LocalDate.parse("2015-06-01"), LocalDate.parse("2020-06-01"));

    public static Subject subjectGetById = new Subject(1, "Calculus", 120, 1, "math");
    public static Subject subjectSave = new Subject("NEW", 120, 1, "math");
    public static Subject subjectUpdate = new Subject(1, "updated", 120, 1, "math");

    public static Teacher teacherGetById = new Teacher(1,"first", "teacher", LocalDate.parse("1990-01-01"),
            "male", "first@gmail.com", "11111", 1,
            Collections.singletonList(new Subject(1, "Calculus", 120, 1, "math")));
    public static Teacher teacherSave = new Teacher("first", "teacher", LocalDate.parse("1990-01-01"),
            "male", "first@gmail.com", "11111",
            Collections.singletonList(new Subject(1, "Calculus", 120, 1, "math")));
    public static Teacher teacherUpdate = new Teacher(1,"UPDATED", "teacher", LocalDate.parse("1990-01-01"),
            "male", "first@gmail.com", "11111", 1,
            Collections.singletonList(new Subject(1, "Calculus", 120, 1, "math")));

    public static Group groupGetById = new Group(1, "AA-11", Collections.singletonList(studentGetById));
    public static Group groupSave = new Group("DD-44");
    public static Group groupUpdate = new Group(1, "UPDATED");

    public static Lesson lessonGetById = new Lesson(1, subjectGetById, teacherGetById, singletonList(groupGetById), audienceGetById, lessonTimeGetById);
    public static Lesson lessonSave = new Lesson(subjectGetById, teacherGetById, singletonList(groupGetById), audienceGetById, lessonTimeGetById);
    public static Lesson lessonUpdate = new Lesson(2, subjectGetById, teacherGetById, singletonList(groupGetById), audienceGetById, lessonTimeGetById);

    public static DaySchedule dayScheduleGetById = new DaySchedule(1, LocalDate.parse("2017-06-01"), singletonList(lessonGetById));
    public static DaySchedule dayScheduleSave = new DaySchedule(LocalDate.parse("2017-06-01"), singletonList(lessonGetById));
    public static DaySchedule dayScheduleUpdate = new DaySchedule(1, LocalDate.parse("3000-01-01"), singletonList(lessonGetById));
}
