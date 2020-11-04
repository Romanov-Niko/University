package com.foxminded.university.repository;

import com.foxminded.university.domain.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.foxminded.university.TestData.retrievedGroup;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

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