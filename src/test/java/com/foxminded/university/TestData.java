package com.foxminded.university;

import com.foxminded.university.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;

public class TestData {

    public static Audience retrievedAudience = new Audience(1, 101, 100);
    public static Audience createdAudience = new Audience(104, 100);
    public static Audience updatedAudience = new Audience(1, 999, 999);

    public static LessonTime retrievedLessonTime = new LessonTime(1, LocalTime.parse("08:00:00"), LocalTime.parse("09:00:00"));
    public static LessonTime createdLessonTime = new LessonTime(LocalTime.parse("12:00:00"), LocalTime.parse("13:00:00"));
    public static LessonTime updatedLessonTime = new LessonTime(1, LocalTime.parse("22:00:00"), LocalTime.parse("23:00:00"));
    public static LessonTime lessonTimeWithWrongTime = new LessonTime(LocalTime.parse("13:00:00"), LocalTime.parse("12:00:00"));

    public static Student retrievedStudent = new Student(1, "first", "student", LocalDate.parse("1990-01-01"),
            "male", "first@gmail.com", "11111", 1, "math", 1,
            LocalDate.parse("2015-06-01"), LocalDate.parse("2020-06-01"));
    public static Student createdStudent = new Student("new", "student", LocalDate.parse("1990-01-01"),
            "male", "first@gmail.com", "11111", 1, "math", 1,
            LocalDate.parse("2015-06-01"), LocalDate.parse("2020-06-01"));
    public static Student updatedStudent = new Student(1, "UPDATED", "STUDENT", LocalDate.parse("1990-01-01"),
            "male", "first@gmail.com", "11111", 1, "math", 1,
            LocalDate.parse("2015-06-01"), LocalDate.parse("2020-06-01"));

    public static Subject retrievedSubject = new Subject(1, "Calculus", 120, 1, "math");
    public static Subject createdSubject = new Subject("NEW", 120, 1, "math");
    public static Subject updatedSubject = new Subject(1, "updated", 120, 1, "math");
    public static List<Subject> allSubjects = Arrays.asList(retrievedSubject, new Subject(2,"Anatomy", 120, 2, "biology"),
            new Subject(3,"The world history", 120, 3, "history"));

    public static Teacher retrievedTeacher = new Teacher(1, "first", "teacher", LocalDate.parse("1990-01-01"),
            "male", "first@gmail.com", "11111",
            singletonList(retrievedSubject));
    public static Teacher createdTeacher = new Teacher("first", "teacher", LocalDate.parse("1990-01-01"),
            "male", "first@gmail.com", "11111",
            singletonList(retrievedSubject));
    public static Teacher updatedTeacher = new Teacher(1, "UPDATED", "teacher", LocalDate.parse("1990-01-01"),
            "male", "first@gmail.com", "11111",
            singletonList(retrievedSubject));

    public static Group retrievedGroup = new Group(1, "AA-11", singletonList(retrievedStudent));
    public static Group createdGroup = new Group("DD-44", singletonList(new Student(1, "first",
            "student", LocalDate.parse("1990-01-01"), "male", "first@gmail.com", "11111",
            1, "math", 1, LocalDate.parse("2015-06-01"), LocalDate.parse("2020-06-01"))));
    public static Group updatedGroup = new Group(1, "UPDATED", singletonList(retrievedStudent));

    public static Lesson retrievedLesson = new Lesson(1, retrievedSubject, retrievedTeacher, singletonList(retrievedGroup),
            retrievedAudience, retrievedLessonTime, LocalDate.parse("2017-06-01"));
    public static Lesson createdLesson = new Lesson(retrievedSubject, retrievedTeacher, singletonList(retrievedGroup),
            retrievedAudience, retrievedLessonTime, LocalDate.parse("2017-06-01"));
    public static Lesson updatedLesson = new Lesson(2, retrievedSubject, retrievedTeacher, singletonList(retrievedGroup),
            retrievedAudience, retrievedLessonTime, LocalDate.parse("3000-01-01"));

    public static DaySchedule retrievedDaySchedule = new DaySchedule(LocalDate.parse("2017-06-01"), singletonList(retrievedLesson));
    public static DaySchedule createdDaySchedule = new DaySchedule(LocalDate.parse("2017-06-01"), singletonList(retrievedLesson));
    public static DaySchedule updatedDaySchedule = new DaySchedule(LocalDate.parse("3000-01-01"), singletonList(retrievedLesson));
}
