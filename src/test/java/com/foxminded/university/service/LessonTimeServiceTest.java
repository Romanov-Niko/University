package com.foxminded.university.service;

import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.domain.LessonTime;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.LessonDurationOutOfBoundsException;
import com.foxminded.university.exception.LessonTimeNotUniqueException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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
    void givenLessonTimeWithIncorrectDuration_whenSave_thenLessonDurationOutOfBoundsExceptionThrown() {
        ReflectionTestUtils.setField(lessonTimeService, "maxLessonDuration", 0);

        Throwable exception = assertThrows(LessonDurationOutOfBoundsException.class, () -> lessonTimeService.save(createdLessonTime));
        assertEquals("Lesson duration is out of bounds", exception.getMessage());
        verify(lessonTimeDao, never()).save(createdLessonTime);
    }

    @Test
    void givenLessonTimeWithExistingBeginAndEnd_whenSave_thenLessonTimeNotUniqueExceptionThrown() {
        ReflectionTestUtils.setField(lessonTimeService, "maxLessonDuration", 90);
        given(lessonTimeDao.getByStartAndEndTime(any(), any())).willReturn(Optional.of(retrievedLessonTime));

        Throwable exception = assertThrows(LessonTimeNotUniqueException.class, () -> lessonTimeService.save(createdLessonTime));
        assertEquals("Lesson time with begin 12:00 and end 13:00 already exist", exception.getMessage());
        verify(lessonTimeDao, never()).save(createdLessonTime);
    }

    @Test
    void givenLessonTimeWithNonExistentId_whenUpdate_thenEntityNotFoundExceptionThrown() {
        ReflectionTestUtils.setField(lessonTimeService, "maxLessonDuration", 90);
        given(lessonTimeDao.getById(anyInt())).willReturn(Optional.empty());

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> lessonTimeService.update(updatedLessonTime));
        assertEquals("Lesson time with id 1 is not present", exception.getMessage());
        verify(lessonTimeDao, never()).update(updatedLessonTime);
    }

    @Test
    void givenLessonTimeWithIncorrectDuration_whenUpdate_thenLessonDurationOutOfBoundsExceptionThrown() {
        ReflectionTestUtils.setField(lessonTimeService, "maxLessonDuration", 0);
        given(lessonTimeDao.getById(anyInt())).willReturn(Optional.of(retrievedLessonTime));

        Throwable exception = assertThrows(LessonDurationOutOfBoundsException.class, () -> lessonTimeService.update(updatedLessonTime));
        assertEquals("Lesson duration is out of bounds", exception.getMessage());
        verify(lessonTimeDao, never()).update(updatedLessonTime);
    }
}