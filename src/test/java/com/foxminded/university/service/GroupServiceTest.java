package com.foxminded.university.service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.domain.Group;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.GroupNameNotUniqueException;
import com.foxminded.university.exception.GroupSizeTooLargeException;
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
        given(groupDao.getByName("DD-44")).willReturn(Optional.empty());
        given(studentDao.getById(1)).willReturn(Optional.of(retrievedStudent));

        groupService.save(createdGroup);

        verify(groupDao, times(1)).save(createdGroup);
    }

    @Test
    void givenGroup_whenUpdate_thenCalledGroupDaoUpdate() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);
        given(groupDao.getById(1)).willReturn(Optional.of(retrievedGroup));
        given(studentDao.getById(1)).willReturn(Optional.of(retrievedStudent));

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
        given(groupDao.getAllByLessonId(1)).willReturn(singletonList(retrievedGroup));

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
    void givenGroupWithExistingName_whenSave_thenGroupNameNotUniqueExceptionThrown() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);
        given(groupDao.getByName("DD-44")).willReturn(Optional.of(retrievedGroup));

        Throwable exception = assertThrows(GroupNameNotUniqueException.class, () -> groupService.save(createdGroup));
        assertEquals("Group with name DD-44 already exist", exception.getMessage());
        verify(groupDao, never()).save(createdGroup);
    }

    @Test
    void givenNonExistentStudentId_whenSave_thenEntityNotFoundExceptionThrown() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> groupService.save(createdGroup));
        assertEquals("Student with id 1 is not present", exception.getMessage());
        verify(groupDao, never()).save(createdGroup);
    }

    @Test
    void givenNonExistentId_whenUpdate_thenEntityNotFoundExceptionThrown() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);
        given(groupDao.getById(1)).willReturn(Optional.empty());

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> groupService.update(updatedGroup));
        assertEquals("Group with id 1 is not present", exception.getMessage());
        verify(groupDao, never()).update(updatedGroup);
    }

    @Test
    void givenNonExistentStudentId_whenUpdate_thenEntityNotFoundExceptionThrown() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);
        given(groupDao.getById(1)).willReturn(Optional.empty());

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> groupService.update(updatedGroup));
        assertEquals("Group with id 1 is not present", exception.getMessage());
        verify(groupDao, never()).update(updatedGroup);
    }

    @Test
    void givenGroupWithGreaterStudentNumberThatMaxGroupCapacity_whenUpdate_thenGroupSizeTooLargeExceptionThrown() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 0);
        given(groupDao.getById(1)).willReturn(Optional.of(retrievedGroup));

        Throwable exception = assertThrows(GroupSizeTooLargeException.class, () -> groupService.update(updatedGroup));
        assertEquals("Group with id 1 have too many students", exception.getMessage());
        verify(groupDao, never()).update(updatedGroup);
    }

    @Test
    void givenDataThatProducesEmptyResult_whenGetAllByLessonId_thenCalledGroupDaoGetAllByLessonIdAndReturnedEmptyList() {
        given(groupDao.getAllByLessonId(1)).willReturn(emptyList());

        List<Group> actualGroups = groupService.getAllByLessonId(1);

        verify(groupDao, times(1)).getAllByLessonId(1);
        assertEquals(emptyList(), actualGroups);
    }
}