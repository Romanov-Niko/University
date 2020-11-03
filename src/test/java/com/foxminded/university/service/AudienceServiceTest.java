package com.foxminded.university.service;

import com.foxminded.university.repository.AudienceRepository;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.exception.AudienceRoomNumberNotUniqueException;
import com.foxminded.university.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class AudienceServiceTest {

    @Mock
    private AudienceRepository audienceRepository;

    @InjectMocks
    private AudienceService audienceService;

    @Test
    void givenNothing_whenGetAll_thenCalledAudienceDaoGetAllAndReturnedAllAudiences() {
        given(audienceRepository.findAll()).willReturn(singletonList(retrievedAudience));

        List<Audience> actualAudiences = audienceService.getAll();

        verify(audienceRepository, times(1)).findAll();
        assertEquals(singletonList(retrievedAudience), actualAudiences);
    }

    @Test
    void givenAudience_whenSave_thenCalledAudienceDaoSave() {
        given(audienceRepository.findByRoomNumber(104)).willReturn(Optional.empty());

        audienceService.save(createdAudience);

        verify(audienceRepository, times(1)).save(createdAudience);
    }

    @Test
    void givenAudience_whenUpdate_thenCalledAudienceDaoUpdate() {
        given(audienceRepository.findById(1)).willReturn(Optional.of(retrievedAudience));

        audienceService.update(updatedAudience);

        verify(audienceRepository, times(1)).save(updatedAudience);
    }

    @Test
    void givenAudienceId_whenDelete_thenCalledAudienceDaoDelete() {
        audienceService.delete(1);

        verify(audienceRepository, times(1)).deleteById(1);
    }

    @Test
    void givenEmptyTable_whenGetAll_thenCalledAudienceDaoGetAllAndReturnedEmptyList() {
        given(audienceRepository.findAll()).willReturn(emptyList());

        List<Audience> actualAudiences = audienceService.getAll();

        verify(audienceRepository, times(1)).findAll();
        assertEquals(emptyList(), actualAudiences);
    }

    @Test
    void givenExistingRoomNumber_whenSave_thenAudienceRoomNumberNotUniqueExceptionThrown() {
        given(audienceRepository.findByRoomNumber(104)).willReturn(Optional.of(retrievedAudience));
        Throwable exception = assertThrows(AudienceRoomNumberNotUniqueException.class, () -> audienceService.save(createdAudience));
        assertEquals("Audience with room number 104 already exist", exception.getMessage());
        verify(audienceRepository, never()).save(createdAudience);
    }

    @Test
    void givenAudienceWithNonExistentId_whenUpdate_thenEntityNotFoundExceptionThrown() {
        given(audienceRepository.findById(1)).willReturn(Optional.empty());
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> audienceService.update(updatedAudience));
        assertEquals("Audience with id 1 is not present", exception.getMessage());
        verify(audienceRepository, never()).save(updatedAudience);
    }
}