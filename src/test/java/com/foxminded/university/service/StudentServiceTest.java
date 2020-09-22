package com.foxminded.university.service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.dao.StudentDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

import static com.foxminded.university.TestData.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDao studentDao;

    @Mock
    private GroupDao groupDao;

    @InjectMocks
    private StudentService studentService;

    @Test
    void getAll() {
        studentService.getAll();

        verify(studentDao, times(1)).getAll();
    }

    @Test
    void save() {
        ReflectionTestUtils.setField(studentService, "maxCourse", 6);

        studentService.save(createdStudent);

        verify(studentDao, times(1)).save(createdStudent);
    }

    @Test
    void update() {
        ReflectionTestUtils.setField(studentService, "maxCourse", 6);
        given(studentDao.getById(anyInt())).willReturn(Optional.of(retrievedStudent));

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