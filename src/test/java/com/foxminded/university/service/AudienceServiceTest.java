package com.foxminded.university.service;

import com.foxminded.university.TestData;
import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.domain.Audience;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

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
    void getAll() {
        audienceService.getAll();

        verify(audienceDao, times(1)).getAll();
    }

    @Test
    void save() {
        audienceService.save(createdAudience);

        verify(audienceDao, times(1)).save(createdAudience);
    }

    @Test
    void update() {
        given(audienceDao.getById(anyInt())).willReturn(Optional.of(retrievedAudience));

        audienceService.update(updatedAudience);

        verify(audienceDao, times(1)).update(updatedAudience);
    }

    @Test
    void delete() {
        audienceService.delete(1);

        verify(audienceDao, times(1)).delete(1);
    }
}