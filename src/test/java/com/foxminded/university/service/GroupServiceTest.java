package com.foxminded.university.service;

import com.foxminded.university.dao.DayScheduleDao;
import com.foxminded.university.dao.GroupDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static com.foxminded.university.TestData.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupDao groupDao;

    @InjectMocks
    private GroupService groupService;

    @Test
    void getById() {
        groupService.getById(1);

        verify(groupDao, times(1)).getById(1);
    }

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