package com.foxminded.university.repository;

import com.foxminded.university.domain.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class HibernateGroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    void givenId1_whenFindAllByLessonId_thenReturnedAllGroupsOfFirstLesson() {
        List<Group> expectedGroups = singletonList(retrievedGroup);

        List<Group> actualGroups = groupRepository.findAllByLessonId(1);

        assertEquals(expectedGroups, actualGroups);
    }

    @Test
    void givenNameOfFirstGroup_whenFindByName_thenReturnedFirstGroup() {
        Group actualGroup = groupRepository.findByName("AA-11").orElse(null);

        assertEquals(retrievedGroup, actualGroup);
    }

    @Test
    void givenNonExistentName_whenFindByName_thenReturnedOptionalEmpty() {
        Optional<Group> actualGroup = groupRepository.findByName("INCORRECT");

        assertEquals(Optional.empty(), actualGroup);
    }
}