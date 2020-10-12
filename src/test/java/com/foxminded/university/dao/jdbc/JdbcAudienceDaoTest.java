package com.foxminded.university.dao.jdbc;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.domain.Audience;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringJUnitConfig(ApplicationTestConfig.class)
class JdbcAudienceDaoTest {

    @Autowired
    private AudienceDao audienceDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenId1_whenGetById_thenReturnedFirstAudience() {
        Audience actualAudience = audienceDao.getById(1).orElse(null);

        assertEquals(retrievedAudience, actualAudience);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedListOfAllAudiences() {
        int expectedRows = audienceDao.getAll().size();

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences");

        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenAudience_whenSave_thenAddedGivenAudience() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences") + 1;

        audienceDao.save(createdAudience);

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenAudience_whenUpdate_thenUpdatedAudienceWithEqualId() {
        audienceDao.update(updatedAudience);

        int actualNumber = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "audiences", String.format(
                "id = %d AND room_number = %d AND capacity = %d",
                updatedAudience.getId(), updatedAudience.getRoomNumber(), updatedAudience.getCapacity()
        ));
        assertEquals(1, actualNumber);
    }

    @Test
    void givenId3_whenDelete_thenDeletedThirdAudience() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences") - 1;

        audienceDao.delete(3);

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenRoomNumber101_whenGetByRoomNumber_thenReturnedFirstAudience() {
        Audience actualAudience = audienceDao.getByRoomNumber(101).orElse(null);

        assertEquals(retrievedAudience, actualAudience);
    }

    @Test
    void givenNonExistentId_whenGetById_thenReturnedOptionalEmpty() {
        Optional<Audience> actualAudience = audienceDao.getById(4);

        assertEquals(Optional.empty(), actualAudience);
    }

    @Test
    void givenNonExistentTable_whenGetAll_thenReturnedEmptyList() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "audiences");

        List<Audience> actualAudiences = audienceDao.getAll();

        assertEquals(emptyList(), actualAudiences);
    }

    @Test
    void givenNonExistentRoomNumber_whenGetByRoomNumber_thenReturnedOptionalEmpty() {
        Optional<Audience> actualAudience = audienceDao.getByRoomNumber(0);

        assertEquals(Optional.empty(), actualAudience);
    }
}