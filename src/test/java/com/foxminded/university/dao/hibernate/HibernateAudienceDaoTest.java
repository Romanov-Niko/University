package com.foxminded.university.dao.hibernate;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.domain.Audience;
import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
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
import static org.junit.jupiter.api.Assertions.assertNull;

@Transactional
@SpringJUnitConfig(ApplicationTestConfig.class)
class HibernateAudienceDaoTest {

    @Autowired
    private AudienceDao audienceDao;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void givenId1_whenGetById_thenReturnedFirstAudience() {
        Audience expectedAudience = sessionFactory.getCurrentSession().find(Audience.class, 1);

        Audience actualAudience = audienceDao.getById(1).orElse(null);

        assertEquals(expectedAudience, actualAudience);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedListOfAllAudiences() {
        List<Audience> expectedAudiences = sessionFactory.getCurrentSession().createNativeQuery(
                "SELECT * FROM audiences", Audience.class).getResultList();

        List<Audience> actualAudiences = audienceDao.getAll();

        assertEquals(expectedAudiences, actualAudiences);
    }

    @Test
    void givenAudience_whenSave_thenAddedGivenAudience() {
        audienceDao.save(createdAudience);

        Audience actualAudience = sessionFactory.getCurrentSession().find(Audience.class, 4);

        assertEquals(createdAudience, actualAudience);
    }

    @Test
    void givenAudience_whenUpdate_thenUpdatedAudienceWithEqualId() {
        audienceDao.update(updatedAudience);

        Audience actualAudience = sessionFactory.getCurrentSession().find(Audience.class, 1);

        assertEquals(updatedAudience, actualAudience);
    }

    @Test
    void givenId3_whenDelete_thenDeletedThirdAudience() {
        audienceDao.delete(3);

        Audience actualAudience = sessionFactory.getCurrentSession().find(Audience.class, 3);

        assertNull(actualAudience);
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
    void givenEmptyTable_whenGetAll_thenReturnedEmptyList() {
        sessionFactory.getCurrentSession().createNativeQuery("DELETE FROM audiences").executeUpdate();

        List<Audience> actualAudiences = audienceDao.getAll();

        assertEquals(emptyList(), actualAudiences);
    }

    @Test
    void givenNonExistentRoomNumber_whenGetByRoomNumber_thenReturnedOptionalEmpty() {
        Optional<Audience> actualAudience = audienceDao.getByRoomNumber(0);

        assertEquals(Optional.empty(), actualAudience);
    }
}