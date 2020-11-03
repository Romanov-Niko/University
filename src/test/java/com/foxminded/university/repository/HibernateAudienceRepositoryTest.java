package com.foxminded.university.repository;

import com.foxminded.university.domain.Audience;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.foxminded.university.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class HibernateAudienceRepositoryTest {

    @Autowired
    private AudienceRepository audienceRepository;

    @Test
    void givenRoomNumber101_whenFindByRoomNumber_thenReturnedFirstAudience() {
        Audience actualAudience = audienceRepository.findByRoomNumber(101).orElse(null);

        assertEquals(retrievedAudience, actualAudience);
    }

    @Test
    void givenNonExistentRoomNumber_whenFindByRoomNumber_thenReturnedOptionalEmpty() {
        Optional<Audience> actualAudience = audienceRepository.findByRoomNumber(0);

        assertEquals(Optional.empty(), actualAudience);
    }
}