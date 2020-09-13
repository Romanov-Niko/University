package com.foxminded.university.dao.jdbc;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.domain.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;

import static com.foxminded.university.EntitiesForTests.*;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql({"/schema.sql", "/data.sql"})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationTestConfig.class})
class JdbcGroupDaoTest {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenId1_whenGetById_thenReturnedFirstGroup() {
        Group actualGroup = groupDao.getById(1);

        assertEquals(retrievedGroup, actualGroup);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedListOfAllGroups() {
        int expectedRows = groupDao.getAll().size();

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenGroup_whenSave_thenAddedGivenGroup() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups") + 1;

        groupDao.save(createdGroup);

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenGroup_whenUpdate_thenUpdatedGroupWithEqualId() {
        groupDao.update(updatedGroup);

        int actualNumber = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", String.format(
                "id = %d AND name = '%s'", updatedGroup.getId(), updatedGroup.getName()));
        assertEquals(1, actualNumber);
    }

    @Test
    void givenId3_whenDelete_thenDeletedThirdGroup() {
        int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups") - 1;

        groupDao.delete(3);

        int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");
        assertEquals(expectedRows, actualRows);
    }

    @Test
    void givenId1_whenGetAllByLessonId_thenReturnedAllGroupsOfFirstLesson() {
        List<Group> expectedGroups = singletonList(retrievedGroup);

        List<Group> actualGroups = groupDao.getAllByLessonId(1);

        assertEquals(expectedGroups, actualGroups);
    }
}