package com.foxminded.university.service;

import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.dao.StudentDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import static com.foxminded.university.TestData.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDao studentDao;

    @InjectMocks
    private StudentService studentService;

    @Test
    void getById() {
        studentService.getById(1);

        verify(studentDao, times(1)).getById(1);
    }

    @Test
    void getAll() {
        studentService.getAll();

        verify(studentDao, times(1)).getAll();
    }

    @Test
    void save() {
        studentService.save(createdStudent);

        verify(studentDao, times(1)).save(createdStudent);
    }

    @Test
    void update() {
        studentService.update(updatedStudent);

        verify(studentDao, times(1)).update(updatedStudent);
    }

    @Test
    void delete() {
        studentService.delete(1);

        verify(studentDao, times(1)).delete(1);
    }

    @Test
    void getAllByGroupId() {
        studentService.getAllByGroupId(1);

        verify(studentDao, times(1)).getAllByGroupId(1);
    }

    @Test
    void getAllByGroupName() {
        studentService.getAllByGroupName(retrievedGroup.getName());

        verify(studentDao, times(1)).getAllByGroupName(retrievedGroup.getName());
    }
}