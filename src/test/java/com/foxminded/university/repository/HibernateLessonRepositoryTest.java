package com.foxminded.university.repository;

import com.foxminded.university.domain.Lesson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class HibernateLessonRepositoryTest {

    @Autowired
    private LessonRepository lessonRepository;

    @Test
    void givenFirstDate_whenFindAllByDate_thenReturnedAllLessonsOfFirstDay() {
        List<Lesson> expectedLessons = singletonList(retrievedLesson);

        List<Lesson> actualLessons = lessonRepository.findAllByDate(LocalDate.parse("2017-06-01"));

        assertEquals(expectedLessons, actualLessons);
    }

    @Test
    void givenFirstLessonData_whenFindAllByTeacherIdDateAndLessonTimeId_thenReturnedFirstLesson() {
        List<Lesson> actualLessons = lessonRepository.findAllByTeacherIdDateAndLessonTimeId(1, LocalDate.parse("2017-06-01"), 1);

        assertEquals(singletonList(retrievedLesson), actualLessons);
    }

    @Test
    void givenFirstLessonData_whenFindAllByAudienceIdDateAndLessonTimeId_thenReturnedFirstLesson() {
        List<Lesson> actualLessons = lessonRepository.findAllByAudienceIdDateAndLessonTimeId(1, LocalDate.parse("2017-06-01"), 1);

        assertEquals(singletonList(retrievedLesson), actualLessons);
    }

    @Test
    void givenNonExistentDate_whenFindAllByDate_thenReturnedEmptyList() {
        List<Lesson> actualLessons = lessonRepository.findAllByDate(LocalDate.parse("2038-06-01"));

        assertEquals(emptyList(), actualLessons);
    }

    @Test
    void givenWrongData_whenFindAllByTeacherIdDateAndLessonTimeId_thenReturnedEmptyList() {
        List<Lesson> actualLessons = lessonRepository.findAllByTeacherIdDateAndLessonTimeId(5, LocalDate.parse("2017-06-01"), 1);

        assertEquals(emptyList(), actualLessons);
    }

    @Test
    void givenWrongData_whenFindAllByAudienceIdDateAndLessonTimeId_thenReturnedEmptyList() {
        List<Lesson> actualLessons = lessonRepository.findAllByAudienceIdDateAndLessonTimeId(5, LocalDate.parse("2017-06-01"), 1);

        assertEquals(emptyList(), actualLessons);
    }

    @Test
    void givenId1AndFirstDay_whenFindByDayForStudent_thenReturnedDayScheduleWithFirstDayAndFirstLesson() {
        List<Lesson> actualLessons = lessonRepository.findByDateForStudent(1, LocalDate.parse("2017-06-01"));

        assertEquals(retrievedDaySchedule.getLessons(), actualLessons);
    }

    @Test
    void givenId1AndFirstDay_whenFindByDayForTeacher_thenReturnedDayScheduleWithFirstDayAndFirstLesson() {
        List<Lesson> actualLessons = lessonRepository.findByDateForTeacher(1, LocalDate.parse("2017-06-01"));

        assertEquals(retrievedDaySchedule.getLessons(), actualLessons);
    }

    @Test
    void givenId1AndFirstDay_whenFindByMonthForStudent_thenReturnedListOfDaysSchedulesWithFirstDayAndFirstLesson() {
        List<Lesson> actualLessons = lessonRepository.findByMonthForStudent(1, LocalDate.parse("2017-06-01"));

        assertEquals(retrievedDaySchedule.getLessons(), actualLessons);
    }

    @Test
    void givenId1AndFirstDay_whenFindByMonthForTeacher_thenReturnedListOfDaysSchedulesWithFirstDayAndFirstLesson() {
        List<Lesson> actualLessons = lessonRepository.findByMonthForStudent(1, LocalDate.parse("2017-06-01"));

        assertEquals(retrievedDaySchedule.getLessons(), actualLessons);
    }

    @Test
    void givenWrongData_whenFindByDayForStudent_thenReturnedScheduleWithoutLessons() {
        List<Lesson> actualLessons = lessonRepository.findByDateForStudent(5, LocalDate.parse("2017-06-01"));

        assertTrue(actualLessons.isEmpty());
    }

    @Test
    void givenWrongData_whenFindByDayForTeacher_thenReturnedOptionalEmpty() {
        List<Lesson> actualLessons = lessonRepository.findByDateForTeacher(5, LocalDate.parse("2017-06-01"));

        assertTrue(actualLessons.isEmpty());
    }

    @Test
    void givenWrongData_whenFindByMonthForStudent_thenReturnedEmptyList() {
        List<Lesson> actualLessons = lessonRepository.findByMonthForStudent(5, LocalDate.parse("2017-06-01"));

        assertEquals(emptyList(), actualLessons);
    }

    @Test
    void givenWrongData_whenFindByMonthForTeacher_thenReturnedEmptyList() {
        List<Lesson> actualLessons = lessonRepository.findByMonthForStudent(5, LocalDate.parse("2017-06-01"));

        assertEquals(emptyList(), actualLessons);
    }
}