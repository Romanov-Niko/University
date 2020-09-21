package com.foxminded.university.service;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.domain.Subject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.foxminded.university.TestData.createdStudent;
import static com.foxminded.university.TestData.updatedStudent;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static com.foxminded.university.TestData.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectDao subjectDao;

    @InjectMocks
    private SubjectService subjectService;

    @Test
    void getAll() {
        subjectService.getAll();

        verify(subjectDao, times(1)).getAll();
    }

    @Test
    void save() {
        subjectService.save(createdSubject);

        verify(subjectDao, times(1)).save(createdSubject);
    }

    @Test
    void update() {
        given(subjectDao.getById(anyInt())).willReturn(Optional.of(retrievedSubject));

        subjectService.update(updatedSubject);

        verify(subjectDao, times(1)).update(updatedSubject);
    }

    @Test
    void delete() {
        subjectService.delete(1);

        verify(subjectDao, times(1)).delete(1);
    }

    @Test
    void getAllByTeacherId() {
        subjectService.getAllByTeacherId(1);

        verify(subjectDao, times(1)).getAllByTeacherId(1);
    }
}