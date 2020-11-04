package com.foxminded.university.service;

import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.Lesson;
import com.foxminded.university.domain.Teacher;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.repository.*;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private AudienceRepository audienceRepository;

    @Mock
    private LessonTimeRepository lessonTimeRepository;

    @InjectMocks
    private LessonService lessonService;

    @Test
    void givenNothing_whenGetAll_thenCalledLessonDaoGetAllAndReturnedAllLessons() {
        given(lessonRepository.findAll()).willReturn(singletonList(retrievedLesson));

        List<Lesson> actualLessons = lessonService.findAll();

        verify(lessonRepository, times(1)).findAll();
        assertEquals(singletonList(retrievedLesson), actualLessons);
    }

    @Test
    void givenLesson_whenSave_thenCalledLessonDaoSave() {
        given(teacherRepository.findById(1)).willReturn(Optional.of(new Teacher(2, "second", "teacher",
                LocalDate.parse("1990-02-01"), "male", "second@gmail.com", "22222", singletonList(retrievedSubject))));
        given(subjectRepository.findById(1)).willReturn(Optional.of(retrievedSubject));
        given(audienceRepository.findById(1)).willReturn(Optional.of(new Audience(2, 102, 100)));
        given(lessonTimeRepository.findById(1)).willReturn(Optional.of(retrievedLessonTime));
        given(groupRepository.findById(1)).willReturn(Optional.of(retrievedGroup));
        given(lessonRepository.findAllByAudienceIdAndDateAndLessonTimeId(1, LocalDate.parse("2017-06-01"), 1)).willReturn(singletonList(createdLesson));
        given(lessonRepository.findAllByTeacherIdAndDateAndLessonTimeId(1, LocalDate.parse("2017-06-01"), 1)).willReturn(singletonList(createdLesson));

        lessonService.save(createdLesson);

        verify(lessonRepository, times(1)).save(createdLesson);
    }

    @Test
    void givenLesson_whenUpdate_thenCalledLessonDaoUpdate() {
        given(lessonRepository.findById(1)).willReturn(Optional.of(retrievedLesson));
        given(teacherRepository.findById(1)).willReturn(Optional.of(new Teacher(2, "second", "teacher",
                LocalDate.parse("1990-02-01"), "male", "second@gmail.com", "22222", singletonList(retrievedSubject))));
        given(subjectRepository.findById(1)).willReturn(Optional.of(retrievedSubject));
        given(audienceRepository.findById(1)).willReturn(Optional.of(new Audience(2, 102, 100)));
        given(lessonTimeRepository.findById(1)).willReturn(Optional.of(retrievedLessonTime));
        given(groupRepository.findById(1)).willReturn(Optional.of(retrievedGroup));
        given(lessonRepository.findAllByAudienceIdAndDateAndLessonTimeId(1, LocalDate.parse("3000-01-01"), 1)).willReturn(singletonList(updatedLesson));
        given(lessonRepository.findAllByTeacherIdAndDateAndLessonTimeId(1, LocalDate.parse("3000-01-01"), 1)).willReturn(singletonList(updatedLesson));

        lessonService.update(updatedLesson);

        verify(lessonRepository, times(1)).save(updatedLesson);
    }

    @Test
    void givenLessonId_whenDelete_thenCalledLessonDaoDelete() {
        lessonService.delete(1);

        verify(lessonRepository, times(1)).deleteById(1);
    }

    @Test
    void givenFirstDay_whenGetAllByDate_thenCalledLessonDaoGetAllByDateAndReturnedLessonsOfGivenDate() {
        given(lessonRepository.findAllByDate(LocalDate.parse("2017-06-01"))).willReturn(singletonList(retrievedLesson));

        List<Lesson> actualLessons = lessonService.findAllByDate(LocalDate.parse("2017-06-01"));

        verify(lessonRepository, times(1)).findAllByDate(LocalDate.parse("2017-06-01"));
        assertEquals(singletonList(retrievedLesson), actualLessons);
    }

    @Test
    void givenEmptyTable_whenGetAll_thenCalledLessonDaoGetAllAndReturnedEmptyList() {
        given(lessonRepository.findAll()).willReturn(emptyList());

        List<Lesson> actualLessons = lessonService.findAll();

        verify(lessonRepository, times(1)).findAll();
        assertEquals(emptyList(), actualLessons);
    }

    @Test
    void givenLessonWithConflictingData_whenSave_thenEntityNotFoundExceptionThrown() {
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> lessonService.save(createdLesson));
        assertEquals("Subject with id 1 is not present", exception.getMessage());
        verify(lessonRepository, never()).save(createdLesson);
    }

    @Test
    void givenLessonWithConflictingData_whenUpdate_thenEntityNotFoundExceptionThrown() {
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> lessonService.update(updatedLesson));
        assertEquals("Lesson with id 1 is not present", exception.getMessage());
        verify(lessonRepository, never()).save(updatedLesson);
    }

    @Test
    void givenDataThatProducesEmptyReturn_whenGetAllByDate_thenCalledLessonDaoGetAllByDateAndReturnedEmptyList() {
        given(lessonRepository.findAllByDate(LocalDate.parse("2017-06-01"))).willReturn(emptyList());

        List<Lesson> actualLessons = lessonService.findAllByDate(LocalDate.parse("2017-06-01"));

        verify(lessonRepository, times(1)).findAllByDate(LocalDate.parse("2017-06-01"));
        assertEquals(emptyList(), actualLessons);
    }
}