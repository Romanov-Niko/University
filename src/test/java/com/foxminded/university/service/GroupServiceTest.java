package com.foxminded.university.service;

import com.foxminded.university.dao.DayScheduleDao;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.StudentDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

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
    void getAll() {
        groupService.getAll();

        verify(groupDao, times(1)).getAll();
    }

    @Test
    void save() {
        groupService.save(createdGroup);

        verify(groupDao, times(1)).save(createdGroup);
    }

    @Test
    void update() {
        given(groupDao.getById(anyInt())).willReturn(Optional.of(retrievedGroup));
        given(studentDao.getAll()).willReturn(singletonList(retrievedStudent));

        groupService.update(updatedGroup);

        verify(groupDao, times(1)).update(updatedGroup);
    }

    @Test
    void delete() {
        groupService.delete(1);

        verify(groupDao, times(1)).delete(1);
    }

    @Test
    void getAllByLessonId() {
        groupService.getAllByLessonId(1);

        verify(groupDao, times(1)).getAllByLessonId(1);
    }
}