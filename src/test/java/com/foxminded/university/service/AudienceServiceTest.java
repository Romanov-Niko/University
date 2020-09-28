package com.foxminded.university.service;

import com.foxminded.university.TestData;
import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.Group;
import com.foxminded.university.exception.AudienceRoomNumberNotUniqueException;
import com.foxminded.university.exception.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static com.foxminded.university.TestData.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AudienceServiceTest {

    @Mock
    private AudienceDao audienceDao;

    @InjectMocks
    private AudienceService audienceService;

    @Test
    void givenNothing_whenGetAll_thenCalledAudienceDaoGetAllAndReturnedAllAudiences() {
        given(audienceDao.getAll()).willReturn(singletonList(retrievedAudience));

        List<Audience> actualAudiences = audienceService.getAll();

        verify(audienceDao, times(1)).getAll();
        assertEquals(singletonList(retrievedAudience), actualAudiences);
    }

    @Test
    void givenAudience_whenSave_thenCalledAudienceDaoSave() {
        given(audienceDao.getByRoomNumber(anyInt())).willReturn(Optional.empty());

        audienceService.save(createdAudience);

        verify(audienceDao, times(1)).save(createdAudience);
    }

    @Test
    void givenAudience_whenUpdate_thenCalledAudienceDaoUpdate() {
        given(audienceDao.getById(anyInt())).willReturn(Optional.of(retrievedAudience));

        audienceService.update(updatedAudience);

        verify(audienceDao, times(1)).update(updatedAudience);
    }

    @Test
    void givenAudienceId_whenDelete_thenCalledAudienceDaoDelete() {
        audienceService.delete(1);

        verify(audienceDao, times(1)).delete(1);
    }

    @Test
    void givenEmptyTable_whenGetAll_thenCalledAudienceDaoGetAllAndReturnedEmptyList() {
        given(audienceDao.getAll()).willReturn(emptyList());

        List<Audience> actualAudiences = audienceService.getAll();

        verify(audienceDao, times(1)).getAll();
        assertEquals(emptyList(), actualAudiences);
    }

    @Test
    void givenExistingRoomNumber_whenSave_thenAudienceRoomNumberNotUniqueExceptionThrown() {
        given(audienceDao.getByRoomNumber(anyInt())).willReturn(Optional.of(retrievedAudience));
        Throwable exception = assertThrows(AudienceRoomNumberNotUniqueException.class, () -> audienceService.save(createdAudience));
        assertEquals("Audience with room number 104 already exist", exception.getMessage());
        verify(audienceDao, never()).save(createdAudience);
    }

    @Test
    void givenAudienceWithNonExistentId_whenUpdate_thenEntityNotFoundExceptionThrown() {
        given(audienceDao.getById(anyInt())).willReturn(Optional.empty());
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> audienceService.update(updatedAudience));
        assertEquals("Audience with id 1 is not present", exception.getMessage());
        verify(audienceDao, never()).update(updatedAudience);
    }
}