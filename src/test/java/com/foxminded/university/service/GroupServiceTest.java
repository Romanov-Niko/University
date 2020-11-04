package com.foxminded.university.service;

import com.foxminded.university.domain.Group;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.GroupNameNotUniqueException;
import com.foxminded.university.exception.GroupSizeTooLargeException;
import com.foxminded.university.repository.GroupRepository;
import com.foxminded.university.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private GroupService groupService;

    @Test
    void givenNothing_whenGetAll_thenCalledGroupDaoGetAllAndReturnedAllGroups() {
        given(groupRepository.findAll()).willReturn(singletonList(retrievedGroup));

        List<Group> actualGroups = groupService.findAll();

        verify(groupRepository, times(1)).findAll();
        assertEquals(singletonList(retrievedGroup), actualGroups);
    }

    @Test
    void givenGroup_whenSave_thenCalledGroupDaoSave() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);
        given(groupRepository.findByName("DD-44")).willReturn(Optional.empty());
        given(studentRepository.findById(1)).willReturn(Optional.of(retrievedStudent));

        groupService.save(createdGroup);

        verify(groupRepository, times(1)).save(createdGroup);
    }

    @Test
    void givenGroup_whenUpdate_thenCalledGroupDaoUpdate() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);
        given(groupRepository.findById(1)).willReturn(Optional.of(retrievedGroup));
        given(studentRepository.findById(1)).willReturn(Optional.of(retrievedStudent));

        groupService.update(updatedGroup);

        verify(groupRepository, times(1)).save(updatedGroup);
    }

    @Test
    void givenGroupId_whenDelete_thenCalledGroupDaoDelete() {
        groupService.delete(1);

        verify(groupRepository, times(1)).deleteById(1);
    }

    @Test
    void givenEmptyTable_whenGetAll_thenCalledGroupDaoGetAllAndReturnedEmptyList() {
        given(groupRepository.findAll()).willReturn(emptyList());

        List<Group> actualGroups = groupService.findAll();

        verify(groupRepository, times(1)).findAll();
        assertEquals(emptyList(), actualGroups);
    }

    @Test
    void givenGroupWithExistingName_whenSave_thenGroupNameNotUniqueExceptionThrown() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);
        given(groupRepository.findByName("DD-44")).willReturn(Optional.of(retrievedGroup));

        Throwable exception = assertThrows(GroupNameNotUniqueException.class, () -> groupService.save(createdGroup));
        assertEquals("Group with name DD-44 already exist", exception.getMessage());
        verify(groupRepository, never()).save(createdGroup);
    }

    @Test
    void givenNonExistentStudentId_whenSave_thenEntityNotFoundExceptionThrown() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> groupService.save(createdGroup));
        assertEquals("Student with id 1 is not present", exception.getMessage());
        verify(groupRepository, never()).save(createdGroup);
    }

    @Test
    void givenNonExistentId_whenUpdate_thenEntityNotFoundExceptionThrown() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);
        given(groupRepository.findById(1)).willReturn(Optional.empty());

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> groupService.update(updatedGroup));
        assertEquals("Group with id 1 is not present", exception.getMessage());
        verify(groupRepository, never()).save(updatedGroup);
    }

    @Test
    void givenNonExistentStudentId_whenUpdate_thenEntityNotFoundExceptionThrown() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);
        given(groupRepository.findById(1)).willReturn(Optional.empty());

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> groupService.update(updatedGroup));
        assertEquals("Group with id 1 is not present", exception.getMessage());
        verify(groupRepository, never()).save(updatedGroup);
    }

    @Test
    void givenGroupWithGreaterStudentNumberThatMaxGroupCapacity_whenUpdate_thenGroupSizeTooLargeExceptionThrown() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 0);
        given(groupRepository.findById(1)).willReturn(Optional.of(retrievedGroup));

        Throwable exception = assertThrows(GroupSizeTooLargeException.class, () -> groupService.update(updatedGroup));
        assertEquals("Group with id 1 have too many students", exception.getMessage());
        verify(groupRepository, never()).save(updatedGroup);
    }
}