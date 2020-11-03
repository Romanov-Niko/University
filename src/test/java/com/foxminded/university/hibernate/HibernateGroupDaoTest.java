package com.foxminded.university.hibernate;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.domain.Audience;
import com.foxminded.university.domain.Group;
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
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Transactional
@SpringJUnitConfig(ApplicationTestConfig.class)
class HibernateGroupDaoTest {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void givenId1_whenGetById_thenReturnedFirstGroup() {
        Group expectedGroup = sessionFactory.getCurrentSession().find(Group.class, 1);

        Group actualGroup = groupDao.getById(1).orElse(null);

        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void givenNothing_whenGetAll_thenReturnedListOfAllGroups() {
        List<Group> expectedGroups = sessionFactory.getCurrentSession().createNativeQuery(
                "SELECT * FROM groups", Group.class).getResultList();

        List<Group> actualGroups = groupDao.getAll();

        assertEquals(expectedGroups, actualGroups);
    }

    @Test
    void givenGroup_whenSave_thenAddedGivenGroup() {
        groupDao.save(createdGroup);

        Group actualGroup = sessionFactory.getCurrentSession().find(Group.class, createdGroup.getId());

        assertEquals(createdGroup, actualGroup);
    }

    @Test
    void givenGroup_whenUpdate_thenUpdatedGroupWithEqualId() {
        groupDao.update(updatedGroup);

        Group actualGroup = sessionFactory.getCurrentSession().find(Group.class, updatedGroup.getId());

        assertEquals(updatedGroup, actualGroup);
    }

    @Test
    void givenId3_whenDelete_thenDeletedThirdGroup() {
        groupDao.delete(3);

        Group actualGroup = sessionFactory.getCurrentSession().find(Group.class, 3);

        assertNull(actualGroup);
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
    void givenEmptyTable_whenGetAll_thenReturnedEmptyList() {
        sessionFactory.getCurrentSession().createNativeQuery("DELETE FROM groups").executeUpdate();

        List<Group> actualGroups = groupDao.getAll();

        assertEquals(emptyList(), actualGroups);
    }

    @Test
    void givenNonExistentName_whenGetByName_thenReturnedOptionalEmpty() {
        Optional<Group> actualGroup = groupDao.getByName("INCORRECT");

        assertEquals(Optional.empty(), actualGroup);
    }
}