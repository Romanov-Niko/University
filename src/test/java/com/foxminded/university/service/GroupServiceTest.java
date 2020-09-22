package com.foxminded.university.service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.domain.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        groupService.save(createdGroup);

        verify(groupDao, times(1)).save(createdGroup);
    }

    @Test
    void givenGroup_whenUpdate_thenCalledGroupDaoUpdate() {
        ReflectionTestUtils.setField(groupService, "maxGroupCapacity", 30);
        given(groupDao.getById(anyInt())).willReturn(Optional.of(retrievedGroup));
        given(studentDao.getAll()).willReturn(singletonList(retrievedStudent));

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
}