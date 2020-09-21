package com.foxminded.university.service;

import com.foxminded.university.dao.*;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

import static com.foxminded.university.TestData.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    void getAll() {
        lessonService.getAll();

        verify(lessonDao, times(1)).getAll();
    }

    @Test
    void save() {
        given(teacherDao.getById(anyInt())).willReturn(Optional.of(new Teacher(2, "second", "teacher",
                LocalDate.parse("1990-02-01"), "male", "second@gmail.com", "22222", singletonList(retrievedSubject))));
        given(subjectDao.getById(anyInt())).willReturn(Optional.of(retrievedSubject));
        given(audienceDao.getById(anyInt())).willReturn(Optional.of(new Audience(2, 102, 100)));
        given(lessonTimeDao.getById(anyInt())).willReturn(Optional.of(retrievedLessonTime));
        given(groupDao.getAll()).willReturn(singletonList(retrievedGroup));

        lessonService.save(createdLesson, retrievedDaySchedule.getId());

        verify(lessonDao, times(1)).save(createdLesson);
    }

    @Test
    void update() {
        given(lessonDao.getById(anyInt())).willReturn(Optional.of(retrievedLesson));
        given(teacherDao.getById(anyInt())).willReturn(Optional.of(new Teacher(2, "second", "teacher",
                LocalDate.parse("1990-02-01"), "male", "second@gmail.com", "22222", singletonList(retrievedSubject))));
        given(subjectDao.getById(anyInt())).willReturn(Optional.of(retrievedSubject));
        given(audienceDao.getById(anyInt())).willReturn(Optional.of(new Audience(2, 102, 100)));
        given(lessonTimeDao.getById(anyInt())).willReturn(Optional.of(retrievedLessonTime));
        given(groupDao.getAll()).willReturn(singletonList(retrievedGroup));

        lessonService.update(updatedLesson, retrievedDaySchedule.getId());

        verify(lessonDao, times(1)).update(updatedLesson);
    }

    @Test
    void delete() {
        lessonService.delete(1);

        verify(lessonDao, times(1)).delete(1);
    }

    @Test
    void getAllByDayId() {
        lessonService.getAllByDayId(1);

        verify(lessonDao, times(1)).getAllByDayId(1);
    }
}