package com.foxminded.university.dao.jdbc;

import com.foxminded.university.EntitiesForTests;
import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Student;
import com.foxminded.university.domain.Subject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Sql({"/schema.sql", "/data.sql"})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationTestConfig.class})
class JdbcGroupDaoTest {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void getById() {
        Group expectedGroup = EntitiesForTests.groupGetById;
        Group actualGroup = groupDao.getById(1);
        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void getAll() {
        int expectedRows = groupDao.getAll().size();
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void save() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups") + 1;
        groupDao.save(EntitiesForTests.groupSave);
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void update() {
        Group groupForUpdate = EntitiesForTests.groupUpdate;
        groupDao.update(groupForUpdate);
        int updatedGroup = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", String.format(
                "group_id = %d AND name = '%s'", groupForUpdate.getId(), groupForUpdate.getName()));
        assertEquals(1, updatedGroup);
    }

    @Test
    void delete() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups") - 1;
        groupDao.delete(3);
        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void getAllByLessonId() {
        List<Group> expectedGroups = new ArrayList<>();
        expectedGroups.add(EntitiesForTests.groupGetById);
        List<Group> actualGroups = groupDao.getAllByLessonId(1);
        assertEquals(expectedGroups, actualGroups);
    }
}