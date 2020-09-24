package com.foxminded.university.service;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.domain.LessonTime;
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
    void givenLessonTimeWithIncorrectDuration_whenSave_thenWasNotCalledLessonTimeDaoSave() {
        ReflectionTestUtils.setField(lessonTimeService, "maxLessonDuration", 0);

        lessonTimeService.save(createdLessonTime);

        verify(lessonTimeDao, never()).save(createdLessonTime);
    }

    @Test
    void givenLessonTimeWithExistingBeginAndEnd_whenSave_thenWasNotCalledLessonTimeDaoSave() {
        ReflectionTestUtils.setField(lessonTimeService, "maxLessonDuration", 90);
        given(lessonTimeDao.getByStartAndEndTime(any(), any())).willReturn(Optional.of(retrievedLessonTime));

        lessonTimeService.save(createdLessonTime);

        verify(lessonTimeDao, never()).save(createdLessonTime);
    }

    @Test
    void givenLessonTimeWithNonExistentId_whenUpdate_thenWasNotCalledLessonTimeDaoUpdate() {
        ReflectionTestUtils.setField(lessonTimeService, "maxLessonDuration", 90);
        given(lessonTimeDao.getById(anyInt())).willReturn(Optional.empty());

        lessonTimeService.update(updatedLessonTime);

        verify(lessonTimeDao, never()).update(updatedLessonTime);
    }

    @Test
    void givenLessonTimeWithIncorrectDuration_whenUpdate_thenWasNotCalledLessonTimeDaoUpdate() {
        ReflectionTestUtils.setField(lessonTimeService, "maxLessonDuration", 0);

        lessonTimeService.update(updatedLessonTime);

        verify(lessonTimeDao, never()).update(updatedLessonTime);
    }
}