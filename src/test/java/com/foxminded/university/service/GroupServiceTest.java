package com.foxminded.university.service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.domain.Group;
import com.foxminded.university.exception.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static com.foxminded.university.TestData.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupDao groupDao;

    @Mock
    private StudentDao studentDao;

    @InjectMocks
    private GroupService groupService;

    @Test
    void givenNothing_whenGetAll_thenCalledGroupDaoGetAllAndReturnedAllGroups() {
        given(groupDao.getAll()).willReturn(singletonList(retrievedGroup));

        List<Group> actualGroups = groupService.getAll();

        verify(groupDao, times(1)).getAll();
        assertEquals(singletonList(retrievedGroup), actualGroups);
    }

    @Test
    void givenGroup_whenSave_thenCalledGroupDaoSave() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);
        given(groupDao.getByName(anyString())).willReturn(Optional.empty());
        given(studentDao.getById(anyInt())).willReturn(Optional.of(retrievedStudent));

        groupService.save(createdGroup);

        verify(groupDao, times(1)).save(createdGroup);
    }

    @Test
    void givenGroup_whenUpdate_thenCalledGroupDaoUpdate() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);
        given(groupDao.getById(anyInt())).willReturn(Optional.of(retrievedGroup));
        given(studentDao.getById(anyInt())).willReturn(Optional.of(retrievedStudent));

        groupService.update(updatedGroup);

        verify(groupDao, times(1)).update(updatedGroup);
    }

    @Test
    void givenGroupId_whenDelete_thenCalledGroupDaoDelete() {
        groupService.delete(1);

        verify(groupDao, times(1)).delete(1);
    }

    @Test
    void givenLessonId_whenGetAllByLessonId_thenCalledGroupDaoGetAllByLessonIdAndReturnedAllGroupsOfGivenLesson() {
        given(groupDao.getAllByLessonId(anyInt())).willReturn(singletonList(retrievedGroup));

        List<Group> actualGroups = groupService.getAllByLessonId(1);

        verify(groupDao, times(1)).getAllByLessonId(1);
        assertEquals(singletonList(retrievedGroup), actualGroups);
    }

    @Test
    void givenEmptyTable_whenGetAll_thenCalledGroupDaoGetAllAndReturnedEmptyList() {
        given(groupDao.getAll()).willReturn(emptyList());

        List<Group> actualGroups = groupService.getAll();

        verify(groupDao, times(1)).getAll();
        assertEquals(emptyList(), actualGroups);
    }

    @Test
    void givenGroupWithExistingName_whenSave_thenWasThrownGroupNameNotUniqueException() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);
        given(groupDao.getByName(anyString())).willReturn(Optional.of(retrievedGroup));

        assertThrows(GroupNameNotUniqueException.class, () -> groupService.save(createdGroup), "Group with name DD-44 already exist");
        verify(groupDao, never()).save(createdGroup);
    }

    @Test
    void givenNonExistentStudentId_whenSave_thenWasThrownEntityNotFoundException() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);

        assertThrows(EntityNotFoundException.class, () -> groupService.save(createdGroup), "There are students who are not present");
        verify(groupDao, never()).save(createdGroup);
    }

    @Test
    void givenNonExistentId_whenUpdate_thenWasThrownEntityNotFoundException() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);
        given(groupDao.getById(anyInt())).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> groupService.update(updatedGroup), "Group is not present");
        verify(groupDao, never()).update(updatedGroup);
    }

    @Test
    void givenNonExistentStudentId_whenUpdate_thenWasThrownEntityNotFoundException() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);
        given(groupDao.getById(anyInt())).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> groupService.update(updatedGroup), "There are students who are not present");
        verify(groupDao, never()).update(updatedGroup);
    }

    @Test
    void givenGroupWithGreaterStudentNumberThatMaxGroupCapacity_whenUpdate_thenWasThrownGroupSizeTooLargeException() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 0);
        given(groupDao.getById(anyInt())).willReturn(Optional.of(retrievedGroup));

        assertThrows(GroupSizeTooLargeException.class, () -> groupService.update(updatedGroup), "Too many students in the group");
        verify(groupDao, never()).update(updatedGroup);
    }

    @Test
    void givenDataThatProducesEmptyResult_whenGetAllByLessonId_thenCalledGroupDaoGetAllByLessonIdAndReturnedEmptyList() {
        given(groupDao.getAllByLessonId(anyInt())).willReturn(emptyList());

        List<Group> actualGroups = groupService.getAllByLessonId(1);

        verify(groupDao, times(1)).getAllByLessonId(1);
        assertEquals(emptyList(), actualGroups);
    }
}