package com.foxminded.university.dao.jdbc;

import com.foxminded.university.EntitiesForTests;
import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.domain.Audience;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Sql({"/schema.sql", "/data.sql"})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationTestConfig.class})
class JdbcAudienceDaoTest {

    @Autowired
    private AudienceDao audienceDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void getById() {
        Audience expectedAudience = EntitiesForTests.audienceGetById;
        Audience actualAudience = audienceDao.getById(1);
        assertEquals(expectedAudience, actualAudience);
    }

    @Test
    void getAll() {
        int expectedRows = audienceDao.getAll().size();
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void save() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences") + 1;
        audienceDao.save(EntitiesForTests.audienceSave);
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void update() {
        Audience audienceForUpdate = EntitiesForTests.audienceUpdate;
        audienceDao.update(audienceForUpdate);
        int updatedAudience = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "audiences", String.format(
                "audience_id = %d AND room_number = %d AND capacity = %d",
                audienceForUpdate.getId(), audienceForUpdate.getRoomNumber(), audienceForUpdate.getCapacity()
        ));
        assertEquals(1, updatedAudience);
    }

    @Test
    void delete() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences") - 1;
        audienceDao.delete(3);
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences");
        assertEquals(expectedRows, actualRows);
    }
}