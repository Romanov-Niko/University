package com.foxminded.university.service;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.LessonTimeDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static com.foxminded.university.TestData.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LessonTimeServiceTest {

    @Mock
    private LessonTimeDao lessonTimeDao;

    @InjectMocks
    private LessonTimeService lessonTimeService;

    @Test
    void getAll() {
        lessonTimeService.getAll();

        verify(lessonTimeDao, times(1)).getAll();
    }

    @Test
    void save() {
        lessonTimeService.save(createdLessonTime);

        verify(lessonTimeDao, times(1)).save(createdLessonTime);
    }

    @Test
    void update() {
        given(lessonTimeDao.getById(anyInt())).willReturn(Optional.of(retrievedLessonTime));

        lessonTimeService.update(updatedLessonTime);

        verify(lessonTimeDao, times(1)).update(updatedLessonTime);
    }

    @Test
    void delete() {
        lessonTimeService.delete(1);

        verify(lessonTimeDao, times(1)).delete(1);
    }
}