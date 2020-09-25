package com.foxminded.university.service;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.domain.LessonTime;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.exception.EntityOutOfBoundsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

import static com.foxminded.university.TestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonTimeServiceTest {

    @Mock
    private LessonTimeDao lessonTimeDao;

    @InjectMocks
    private LessonTimeService lessonTimeService;

    @Test
    void givenNothing_whenGetAll_thenCalledLessonTimeDaoGetAllAndReturnedAllLessonsTimes() {
        given(lessonTimeDao.getAll()).willReturn(singletonList(retrievedLessonTime));

        List<LessonTime> actualLessonsTimes =  lessonTimeService.getAll();

        verify(lessonTimeDao, times(1)).getAll();
        assertEquals(singletonList(retrievedLessonTime), actualLessonsTimes);
    }

    @Test
    void givenLessonTime_whenSave_thenCalledLessonTimeDaoSave() {
        ReflectionTestUtils.setField(lessonTimeService, "maxLessonDuration", 90);
        given(lessonTimeDao.getByStartAndEndTime(any(), any())).willReturn(Optional.empty());

        lessonTimeService.save(createdLessonTime);

        verify(lessonTimeDao, times(1)).save(createdLessonTime);
    }

    @Test
    void givenLessonTime_whenUpdate_thenCalledLessonTimeDaoUpdate() {
        ReflectionTestUtils.setField(lessonTimeService, "maxLessonDuration", 90);
        given(lessonTimeDao.getById(anyInt())).willReturn(Optional.of(retrievedLessonTime));

        lessonTimeService.update(updatedLessonTime);

        verify(lessonTimeDao, times(1)).update(updatedLessonTime);
    }

    @Test
    void givenLessonTimeId_whenDelete_thenCalledLessonTimeDaoDelete() {
        lessonTimeService.delete(1);

        verify(lessonTimeDao, times(1)).delete(1);
    }

    @Test
    void givenEmptyTable_whenGetAll_thenCalledLessonTimeDaoGetAllAndReturnedEmptyList() {
        given(lessonTimeDao.getAll()).willReturn(emptyList());

        List<LessonTime> actualLessonsTimes =  lessonTimeService.getAll();

        verify(lessonTimeDao, times(1)).getAll();
        assertEquals(emptyList(), actualLessonsTimes);
    }

    @Test
    void givenLessonTimeWithIncorrectDuration_whenSave_thenWasThrownEntityOutOfBoundsException() {
        ReflectionTestUtils.setField(lessonTimeService, "maxLessonDuration", 0);

        assertThrows(EntityOutOfBoundsException.class, () -> lessonTimeService.save(createdLessonTime), "Lesson time duration is out of bounds");
        verify(lessonTimeDao, never()).save(createdLessonTime);
    }

    @Test
    void givenLessonTimeWithExistingBeginAndEnd_whenSave_thenWasThrownEntityNotUniqueException() {
        ReflectionTestUtils.setField(lessonTimeService, "maxLessonDuration", 90);
        given(lessonTimeDao.getByStartAndEndTime(any(), any())).willReturn(Optional.of(retrievedLessonTime));

        assertThrows(EntityNotUniqueException.class, () -> lessonTimeService.save(createdLessonTime), "Lesson time with begin 12:00 and end 13:00 already exist");
        verify(lessonTimeDao, never()).save(createdLessonTime);
    }

    @Test
    void givenLessonTimeWithNonExistentId_whenUpdate_thenWasThrownEntityNotFoundException() {
        ReflectionTestUtils.setField(lessonTimeService, "maxLessonDuration", 90);
        given(lessonTimeDao.getById(anyInt())).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> lessonTimeService.update(updatedLessonTime), "Lesson time is not present");
        verify(lessonTimeDao, never()).update(updatedLessonTime);
    }

    @Test
    void givenLessonTimeWithIncorrectDuration_whenUpdate_thenWasThrownEntityOutOfBoundsException() {
        ReflectionTestUtils.setField(lessonTimeService, "maxLessonDuration", 0);
        given(lessonTimeDao.getById(anyInt())).willReturn(Optional.of(retrievedLessonTime));

        assertThrows(EntityOutOfBoundsException.class, () -> lessonTimeService.update(updatedLessonTime), "Lesson time duration is out of bounds");
        verify(lessonTimeDao, never()).update(updatedLessonTime);
    }
}