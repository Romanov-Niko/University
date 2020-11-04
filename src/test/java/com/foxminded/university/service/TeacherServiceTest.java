package com.foxminded.university.service;

import com.foxminded.university.domain.Teacher;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.repository.SubjectRepository;
import com.foxminded.university.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void givenNothing_whenGetAll_thenCalledTeacherDaoGetAllAndReturnedAllTeachers() {
        given(teacherRepository.findAll()).willReturn(singletonList(retrievedTeacher));

        List<Teacher> actualTeachers = teacherService.findAll();

        verify(teacherRepository, times(1)).findAll();
        assertEquals(singletonList(retrievedTeacher), actualTeachers);
    }

    @Test
    void givenTeacher_whenSave_thenCalledTeacherDaoSave() {
        given(subjectRepository.findById(1)).willReturn(Optional.of(retrievedSubject));

        teacherService.save(createdTeacher);

        verify(teacherRepository, times(1)).save(createdTeacher);
    }

    @Test
    void givenTeacher_whenUpdate_thenCalledTeacherDaoUpdate() {
        given(subjectRepository.findById(1)).willReturn(Optional.of(retrievedSubject));
        given(teacherRepository.findById(1)).willReturn(Optional.of(updatedTeacher));

        teacherService.update(updatedTeacher);

        verify(teacherRepository, times(1)).save(updatedTeacher);
    }

    @Test
    void givenTeacherId_whenDelete_thenCalledTeacherDaoDelete() {
        teacherService.delete(1);

        verify(teacherRepository, times(1)).deleteById(1);
    }

    @Test
    void givenEmptyTable_whenGetAll_thenCalledTeacherDaoGetAllAndReturnedEmptyList() {
        given(teacherRepository.findAll()).willReturn(emptyList());

        List<Teacher> actualTeachers = teacherService.findAll();

        verify(teacherRepository, times(1)).findAll();
        assertEquals(emptyList(), actualTeachers);
    }

    @Test
    void givenNonExistentSubjectId_whenSave_thenEntityNotFoundExceptionThrown() {
        given(subjectRepository.findById(1)).willReturn(Optional.empty());

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> teacherService.save(createdTeacher));
        assertEquals("Subject with id 1 is not present", exception.getMessage());
        verify(teacherRepository, never()).save(createdTeacher);
    }

    @Test
    void givenNonExistentTeacherId_whenUpdate_thenEntityNotFoundExceptionThrown() {
        given(teacherRepository.findById(1)).willReturn(Optional.empty());

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> teacherService.update(updatedTeacher));
        assertEquals("Teacher with id 1 is not present", exception.getMessage());
        verify(teacherRepository, never()).save(updatedTeacher);
    }

    @Test
    void givenNonExistentSubjectId_whenUpdate_thenEntityNotFoundExceptionThrown() {
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> teacherService.update(updatedTeacher));
        assertEquals("Teacher with id 1 is not present", exception.getMessage());
        verify(teacherRepository, never()).save(updatedTeacher);
    }
}