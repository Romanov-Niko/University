package com.foxminded.university.service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.TeacherDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.foxminded.university.TestData.createdGroup;
import static com.foxminded.university.TestData.updatedGroup;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static com.foxminded.university.TestData.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherDao teacherDao;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void getById() {
        teacherService.getById(1);

        verify(teacherDao, times(1)).getById(1);
    }

    @Test
    void getAll() {
        teacherService.getAll();

        verify(teacherDao, times(1)).getAll();
    }

    @Test
    void save() {
        teacherService.save(createdTeacher);

        verify(teacherDao, times(1)).save(createdTeacher);
    }

    @Test
    void update() {
        teacherService.update(updatedTeacher);

        verify(teacherDao, times(1)).update(updatedTeacher);
    }

    @Test
    void delete() {
        teacherService.delete(1);

        verify(teacherDao, times(1)).delete(1);
    }
}