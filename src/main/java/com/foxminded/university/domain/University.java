package com.foxminded.university.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class University {

    private List<Student> students;
    private List<Teacher> teachers;
    private List<Audience> audiences;
    private List<Group> groups;
    private List<DaySchedule> schedule;

    public University(List<Student> students, List<Teacher> teachers, List<Group> groups, List<DaySchedule> schedule) {
        this.students = students;
        this.teachers = teachers;
        this.groups = groups;
        this.schedule = schedule;
    }

    public University(List<Student> students, List<Teacher> teachers, List<Audience> audiences, List<Group> groups, List<DaySchedule> schedule) {
        this(students, teachers, groups, schedule);
        this.audiences = audiences;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Audience> getAudiences() {
        return audiences;
    }

    public void setAudiences(List<Audience> audiences) {
        this.audiences = audiences;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<DaySchedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<DaySchedule> schedule) {
        this.schedule = schedule;
    }

    public List<Lesson> getScheduleByDay(Student student, LocalDate day) {
        return getLessonsByDay(day).stream()
                .filter(lesson -> lesson.getGroups().contains(student.getGroup()))
                .collect(toList());
    }

    public List<Lesson> getScheduleByDay(Teacher teacher, LocalDate day) {
        return getLessonsByDay(day).stream()
                .filter(lesson -> lesson.getTeacher().equals(teacher))
                .collect(toList());
    }

    public List<DaySchedule> getScheduleByMonth(Student student, LocalDate day) {
        List<DaySchedule> result = new ArrayList<>();
        List<DaySchedule> nextMonth = getNextMonth(day);
        for (DaySchedule dayOfNextMonth : nextMonth) {
            List<Lesson> lessons = getScheduleByDay(student, dayOfNextMonth.getDay());
            if (!lessons.isEmpty()) {
                result.add(new DaySchedule(dayOfNextMonth.getDay(), lessons));
            }
        }
        return result;
    }

    public List<DaySchedule> getScheduleByMonth(Teacher teacher, LocalDate day) {
        List<DaySchedule> result = new ArrayList<>();
        List<DaySchedule> nextMonth = getNextMonth(day);
        for (DaySchedule dayOfNextMonth : nextMonth) {
            List<Lesson> lessons = getScheduleByDay(teacher, dayOfNextMonth.getDay());
            if (!lessons.isEmpty()) {
                result.add(new DaySchedule(dayOfNextMonth.getDay(), lessons));
            }
        }
        return result;
    }

    private List<Lesson> getLessonsByDay(LocalDate day) {
        for (DaySchedule daySchedule : this.schedule) {
            if (daySchedule.getDay().equals(day)) {
                return daySchedule.getLessons();
            }
        }
        return new ArrayList<>();
    }

    private List<DaySchedule> getNextMonth(LocalDate day) {
        LocalDate dayInNextMonth = day.plusMonths(1);
        return this.schedule.stream()
                .filter(daySchedule -> ((dayInNextMonth.compareTo(daySchedule.getDay()) <= 31) && (dayInNextMonth.compareTo(daySchedule.getDay()) >= 0)))
                .collect(toList());
    }
}
