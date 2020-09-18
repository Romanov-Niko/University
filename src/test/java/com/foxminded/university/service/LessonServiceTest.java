package com.foxminded.university.service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.LessonDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import static com.foxminded.university.TestData.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock
    private LessonDao lessonDao;

    @InjectMocks
    private LessonService lessonService;

    @Test
    void getById() {
        lessonService.getById(1);

        verify(lessonDao, times(1)).getById(1);
    }

    @Test
    void getAll() {
        lessonService.getAll();

        verify(lessonDao, times(1)).getAll();
    }

    @Test
    void save() {
        lessonService.save(createdLesson);

        verify(lessonDao, times(1)).save(createdLesson);
    }

    @Test
    void update() {
        lessonService.update(updatedLesson);

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