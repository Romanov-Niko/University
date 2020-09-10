package com.foxminded.university.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UniversityTest {

    private University university;
    private List<Student> students;
    private List<Teacher> teachers;
    private List<Lesson> lessons;
    private List<DaySchedule> daySchedules;

    @BeforeEach
    public void setUp() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group("A"));
        groups.add(new Group("B"));
        groups.add(new Group("C"));
        students = new ArrayList<>();
        students.add(new Student("FIRST", "STUDENT", 1));
        students.add(new Student("SECOND", "STUDENT", 2));
        students.add(new Student("THIRD", "STUDENT", 3));
        teachers = new ArrayList<>();
        teachers.add(new Teacher("FIRST", "TEACHER"));
        teachers.add(new Teacher("SECOND", "TEACHER"));
        teachers.add(new Teacher("THIRD", "TEACHER"));
        lessons = new ArrayList<>();
        lessons.add(new Lesson(teachers.get(0), groups.subList(0, 1)));
        lessons.add(new Lesson(teachers.get(1), groups.subList(1, 2)));
        lessons.add(new Lesson(teachers.get(2), groups.subList(2, 3)));
        daySchedules = new ArrayList<>();
        daySchedules.add(new DaySchedule(LocalDate.parse("2018-11-01"), lessons));
        daySchedules.add(new DaySchedule(LocalDate.parse("2018-11-02"), lessons));
        daySchedules.add(new DaySchedule(LocalDate.parse("2018-11-03"), lessons));
        daySchedules.add(new DaySchedule(LocalDate.parse("2018-11-04"), lessons));
        daySchedules.add(new DaySchedule(LocalDate.parse("2018-11-05"), lessons));
        university = new University(students, teachers, groups, daySchedules);
    }

    @Test
    void givenFirstStudentAndFirstDay_whenGetScheduleByDay_thenReturnedFirstLesson() {
        List<Lesson> expectedLessons = new ArrayList<>();
        expectedLessons.add(lessons.get(0));

        List<Lesson> actualLessons = university.getScheduleByDay(students.get(0), daySchedules.get(0).getDay());

        assertEquals(expectedLessons, actualLessons);
    }

    @Test
    void givenSecondTeacherAndSecondDay_whenGetScheduleByDay_thenReturnedSecondLesson() {
        List<Lesson> expectedLessons = new ArrayList<>();
        expectedLessons.add(lessons.get(1));

        List<Lesson> actualLessons = university.getScheduleByDay(teachers.get(1), daySchedules.get(1).getDay());

        assertEquals(expectedLessons, actualLessons);
    }

    @Test
    void givenFirstStudentAndFirstDay_whenGetScheduleByMonth_thenReturnedFiveDaySchedulesWithFirstLesson() {
        List<DaySchedule> expectedSchedules = new ArrayList<>();
        expectedSchedules.add(new DaySchedule(LocalDate.parse("2018-11-01"), lessons.subList(0, 1)));
        expectedSchedules.add(new DaySchedule(LocalDate.parse("2018-11-02"), lessons.subList(0, 1)));
        expectedSchedules.add(new DaySchedule(LocalDate.parse("2018-11-03"), lessons.subList(0, 1)));
        expectedSchedules.add(new DaySchedule(LocalDate.parse("2018-11-04"), lessons.subList(0, 1)));
        expectedSchedules.add(new DaySchedule(LocalDate.parse("2018-11-05"), lessons.subList(0, 1)));

        List<DaySchedule> actualSchedules = university.getScheduleByMonth(students.get(0), LocalDate.parse("2018-11-01"));

        assertEquals(expectedSchedules, actualSchedules);
    }

    @Test
    void givenSecondTeacherAndFirstDay_whenGetScheduleByMonth_thenReturnedFiveDaySchedulesWithSecondLesson() {
        List<DaySchedule> expectedSchedules = new ArrayList<>();
        expectedSchedules.add(new DaySchedule(LocalDate.parse("2018-11-01"), lessons.subList(1, 2)));
        expectedSchedules.add(new DaySchedule(LocalDate.parse("2018-11-02"), lessons.subList(1, 2)));
        expectedSchedules.add(new DaySchedule(LocalDate.parse("2018-11-03"), lessons.subList(1, 2)));
        expectedSchedules.add(new DaySchedule(LocalDate.parse("2018-11-04"), lessons.subList(1, 2)));
        expectedSchedules.add(new DaySchedule(LocalDate.parse("2018-11-05"), lessons.subList(1, 2)));

        List<DaySchedule> actualSchedules = university.getScheduleByMonth(teachers.get(1), LocalDate.parse("2018-11-01"));

        assertEquals(expectedSchedules, actualSchedules);
    }
}