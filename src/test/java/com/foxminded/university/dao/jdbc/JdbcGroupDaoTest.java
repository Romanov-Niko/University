package com.foxminded.university.dao.jdbc;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringJUnitConfig(ApplicationTestConfig.class)
class JdbcGroupDaoTest {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenId1_whenGetById_thenReturnedFirstGroup() {
        Group actualGroup = groupDao.getById(1).orElse(null);

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

    @Test
    void givenNameOfFirstGroup_whenGetByName_thenReturnedFirstGroup() {
        Group actualGroup = groupDao.getByName("AA-11").orElse(null);

        assertEquals(retrievedGroup, actualGroup);
    }

    @Test
    void givenNonExistentId_whenGetById_thenReturnedOptionalEmpty() {
        Optional<Group> actualGroup = groupDao.getById(4);

        assertEquals(Optional.empty(), actualGroup);
    }

    @Test
    void givenNonExistentTable_whenGetAll_thenReturnedEmptyList() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "groups");

        List<Group> actualGroups = groupDao.getAll();

        assertEquals(emptyList(), actualGroups);
    }

    @Test
    void givenNonExistentName_whenGetByName_thenReturnedOptionalEmpty() {
        Optional<Group> actualGroup = groupDao.getByName("INCORRECT");

        assertEquals(Optional.empty(), actualGroup);
    }
}