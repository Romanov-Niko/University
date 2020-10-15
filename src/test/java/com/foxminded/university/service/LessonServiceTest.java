package com.foxminded.university.service;

import com.foxminded.university.dao.*;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.Lesson;
import com.foxminded.university.domain.Teacher;
import com.foxminded.university.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock
    private LessonDao lessonDao;

    @Mock
    private TeacherDao teacherDao;

    @Mock
    private SubjectDao subjectDao;

    @Mock
    private GroupDao groupDao;

    @Mock
    private AudienceDao audienceDao;

    @Mock
    private LessonTimeDao lessonTimeDao;

    @InjectMocks
    private LessonService lessonService;

    @Test
    void givenNothing_whenGetAll_thenCalledLessonDaoGetAllAndReturnedAllLessons() {
        given(lessonDao.getAll()).willReturn(singletonList(retrievedLesson));

        List<Lesson> actualLessons = lessonService.getAll();

        verify(lessonDao, times(1)).getAll();
        assertEquals(singletonList(retrievedLesson), actualLessons);
    }

    @Test
    void givenLesson_whenSave_thenCalledLessonDaoSave() {
        given(teacherDao.getById(1)).willReturn(Optional.of(new Teacher(2, "second", "teacher",
                LocalDate.parse("1990-02-01"), "male", "second@gmail.com", "22222", singletonList(retrievedSubject))));
        given(subjectDao.getById(1)).willReturn(Optional.of(retrievedSubject));
        given(audienceDao.getById(1)).willReturn(Optional.of(new Audience(2, 102, 100)));
        given(lessonTimeDao.getById(1)).willReturn(Optional.of(retrievedLessonTime));
        given(groupDao.getById(1)).willReturn(Optional.of(retrievedGroup));
        given(lessonDao.getAllByAudienceIdDateAndLessonTimeId(1, LocalDate.parse("2017-06-01"), 1)).willReturn(singletonList(createdLesson));
        given(lessonDao.getAllByTeacherIdDateAndLessonTimeId(1, LocalDate.parse("2017-06-01"), 1)).willReturn(singletonList(createdLesson));

        lessonService.save(createdLesson);

        verify(lessonDao, times(1)).save(createdLesson);
    }

    @Test
    void givenLesson_whenUpdate_thenCalledLessonDaoUpdate() {
        given(lessonDao.getById(2)).willReturn(Optional.of(retrievedLesson));
        given(teacherDao.getById(1)).willReturn(Optional.of(new Teacher(2, "second", "teacher",
                LocalDate.parse("1990-02-01"), "male", "second@gmail.com", "22222", singletonList(retrievedSubject))));
        given(subjectDao.getById(1)).willReturn(Optional.of(retrievedSubject));
        given(audienceDao.getById(1)).willReturn(Optional.of(new Audience(2, 102, 100)));
        given(lessonTimeDao.getById(1)).willReturn(Optional.of(retrievedLessonTime));
        given(groupDao.getById(1)).willReturn(Optional.of(retrievedGroup));
        given(lessonDao.getAllByAudienceIdDateAndLessonTimeId(1, LocalDate.parse("3000-01-01"), 1)).willReturn(singletonList(updatedLesson));
        given(lessonDao.getAllByTeacherIdDateAndLessonTimeId(1, LocalDate.parse("3000-01-01"), 1)).willReturn(singletonList(updatedLesson));

        lessonService.update(updatedLesson);

        verify(lessonDao, times(1)).update(updatedLesson);
    }

    @Test
    void givenLessonId_whenDelete_thenCalledLessonDaoDelete() {
        lessonService.delete(1);

        verify(lessonDao, times(1)).delete(1);
    }

    @Test
    void givenFirstDay_whenGetAllByDate_thenCalledLessonDaoGetAllByDateAndReturnedLessonsOfGivenDate() {
        given(lessonDao.getAllByDate(LocalDate.parse("2017-06-01"))).willReturn(singletonList(retrievedLesson));

        List<Lesson> actualLessons = lessonService.getAllByDate(LocalDate.parse("2017-06-01"));

        verify(lessonDao, times(1)).getAllByDate(LocalDate.parse("2017-06-01"));
        assertEquals(singletonList(retrievedLesson), actualLessons);
    }

    @Test
    void givenEmptyTable_whenGetAll_thenCalledLessonDaoGetAllAndReturnedEmptyList() {
        given(lessonDao.getAll()).willReturn(emptyList());

        List<Lesson> actualLessons = lessonService.getAll();

        verify(lessonDao, times(1)).getAll();
        assertEquals(emptyList(), actualLessons);
    }

    @Test
    void givenLessonWithConflictingData_whenSave_thenEntityNotFoundExceptionThrown() {
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> lessonService.save(createdLesson));
        assertEquals("Subject with id 1 is not present", exception.getMessage());
        verify(lessonDao, never()).save(createdLesson);
    }

    @Test
    void givenLessonWithConflictingData_whenUpdate_thenEntityNotFoundExceptionThrown() {
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> lessonService.update(updatedLesson));
        assertEquals("Lesson with id 2 is not present", exception.getMessage());
        verify(lessonDao, never()).update(updatedLesson);
    }

    @Test
    void givenDataThatProducesEmptyReturn_whenGetAllByDate_thenCalledLessonDaoGetAllByDateAndReturnedEmptyList() {
        given(lessonDao.getAllByDate(LocalDate.parse("2017-06-01"))).willReturn(emptyList());

        List<Lesson> actualLessons = lessonService.getAllByDate(LocalDate.parse("2017-06-01"));

        verify(lessonDao, times(1)).getAllByDate(LocalDate.parse("2017-06-01"));
        assertEquals(emptyList(), actualLessons);
    }
}