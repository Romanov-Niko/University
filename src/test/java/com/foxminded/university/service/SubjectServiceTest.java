package com.foxminded.university.service;

import com.foxminded.university.domain.Subject;
import com.foxminded.university.exception.CourseNumberOutOfBoundsException;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.SubjectNameNotUniqueException;
import com.foxminded.university.repository.SubjectRepository;
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
class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectService subjectService;

    @Test
    void givenNothing_whenGetAll_thenCalledSubjectDaoGetAllAndReturnedAllSubjects() {
        given(subjectRepository.findAll()).willReturn(singletonList(retrievedSubject));

        List<Subject> actualSubjects = subjectService.findAll();

        verify(subjectRepository, times(1)).findAll();
        assertEquals(singletonList(retrievedSubject), actualSubjects);
    }

    @Test
    void givenSubject_whenSave_thenCalledSubjectDaoSave() {
        ReflectionTestUtils.setField(subjectService, "maxCourse", 6);
        given(subjectRepository.findByName("NEW")).willReturn(Optional.empty());

        subjectService.save(createdSubject);

        verify(subjectRepository, times(1)).save(createdSubject);
    }

    @Test
    void givenSubject_whenUpdate_thenCalledSubjectDaoUpdate() {
        ReflectionTestUtils.setField(subjectService, "maxCourse", 6);
        given(subjectRepository.findById(1)).willReturn(Optional.of(retrievedSubject));

        subjectService.update(updatedSubject);

        verify(subjectRepository, times(1)).save(updatedSubject);
    }

    @Test
    void givenSubjectId_whenDelete_thenCalledSubjectDaoDelete() {
        subjectService.delete(1);

        verify(subjectRepository, times(1)).deleteById(1);
    }

    @Test
    void givenEmptyTable_whenGetAll_thenCalledSubjectDaoGetAllAndReturnedEmptyList() {
        given(subjectRepository.findAll()).willReturn(emptyList());

        List<Subject> actualSubjects = subjectService.findAll();

        verify(subjectRepository, times(1)).findAll();
        assertEquals(emptyList(), actualSubjects);
    }

    @Test
    void givenSubjectWithExistingName_whenSave_thenSubjectNameNotUniqueExceptionThrown() {
        ReflectionTestUtils.setField(subjectService, "maxCourse", 6);
        given(subjectRepository.findByName("NEW")).willReturn(Optional.of(retrievedSubject));

        Throwable exception = assertThrows(SubjectNameNotUniqueException.class, () -> subjectService.save(createdSubject));
        assertEquals("Subject with name NEW already exist", exception.getMessage());
        verify(subjectRepository, never()).save(createdSubject);
    }

    @Test
    void givenSubjectWithIncorrectCourse_whenSave_thenCourseNumberOutOfBoundsExceptionThrown() {
        ReflectionTestUtils.setField(subjectService, "maxCourse", 0);

        Throwable exception = assertThrows(CourseNumberOutOfBoundsException.class, () -> subjectService.save(createdSubject));
        assertEquals("Course number is out of bounds", exception.getMessage());
        verify(subjectRepository, never()).save(createdSubject);
    }

    @Test
    void givenSubjectWithNonExistentId_whenUpdate_thenEntityNotFoundExceptionThrown() {
        ReflectionTestUtils.setField(subjectService, "maxCourse", 6);
        given(subjectRepository.findById(1)).willReturn(Optional.empty());

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> subjectService.update(updatedSubject));
        assertEquals("Subject with id 1 is not present", exception.getMessage());
        verify(subjectRepository, never()).save(updatedSubject);
    }

    @Test
    void givenSubjectWithIncorrectCourse_whenUpdate_thenCourseNumberOutOfBoundsExceptionThrown() {
        ReflectionTestUtils.setField(subjectService, "maxCourse", 0);
        given(subjectRepository.findById(1)).willReturn(Optional.of(retrievedSubject));

        Throwable exception = assertThrows(CourseNumberOutOfBoundsException.class, () -> subjectService.update(updatedSubject));
        assertEquals("Course number is out of bounds", exception.getMessage());
        verify(subjectRepository, never()).save(updatedSubject);
    }
}