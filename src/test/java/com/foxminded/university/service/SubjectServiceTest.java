package com.foxminded.university.service;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.domain.Subject;
import com.foxminded.university.exception.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static com.foxminded.university.TestData.createdStudent;
import static com.foxminded.university.TestData.updatedStudent;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import static com.foxminded.university.TestData.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectDao subjectDao;

    @InjectMocks
    private SubjectService subjectService;

    @Test
    void givenNothing_whenGetAll_thenCalledSubjectDaoGetAllAndReturnedAllSubjects() {
        given(subjectDao.getAll()).willReturn(singletonList(retrievedSubject));

        List<Subject> actualSubjects = subjectService.getAll();

        verify(subjectDao, times(1)).getAll();
        assertEquals(singletonList(retrievedSubject), actualSubjects);
    }

    @Test
    void givenSubject_whenSave_thenCalledSubjectDaoSave() {
        ReflectionTestUtils.setField(subjectService, "maxCourse", 6);
        given(subjectDao.getByName(anyString())).willReturn(Optional.empty());

        subjectService.save(createdSubject);

        verify(subjectDao, times(1)).save(createdSubject);
    }

    @Test
    void givenSubject_whenUpdate_thenCalledSubjectDaoUpdate() {
        ReflectionTestUtils.setField(subjectService, "maxCourse", 6);
        given(subjectDao.getById(anyInt())).willReturn(Optional.of(retrievedSubject));

        subjectService.update(updatedSubject);

        verify(subjectDao, times(1)).update(updatedSubject);
    }

    @Test
    void givenSubjectId_whenDelete_thenCalledSubjectDaoDelete() {
        subjectService.delete(1);

        verify(subjectDao, times(1)).delete(1);
    }

    @Test
    void givenTeacherId_whenGetAllByTeacherId_thenCalledSubjectDaoGetAllByTeacherIdAndReturnedAllSubjectsOfGivenTeacher() {
        given(subjectDao.getAllByTeacherId(anyInt())).willReturn(singletonList(retrievedSubject));

        List<Subject> actualSubjects = subjectService.getAllByTeacherId(1);

        verify(subjectDao, times(1)).getAllByTeacherId(1);
        assertEquals(singletonList(retrievedSubject), actualSubjects);
    }

    @Test
    void givenEmptyTable_whenGetAll_thenCalledSubjectDaoGetAllAndReturnedEmptyList() {
        given(subjectDao.getAll()).willReturn(emptyList());

        List<Subject> actualSubjects = subjectService.getAll();

        verify(subjectDao, times(1)).getAll();
        assertEquals(emptyList(), actualSubjects);
    }

    @Test
    void givenSubjectWithExistingName_whenSave_thenWasThrownSubjectNameNotUniqueException() {
        ReflectionTestUtils.setField(subjectService, "maxCourse", 6);
        given(subjectDao.getByName(anyString())).willReturn(Optional.of(retrievedSubject));

        assertThrows(SubjectNameNotUniqueException.class, () -> subjectService.save(createdSubject), "Subject with name NEW already exist");
        verify(subjectDao, never()).save(createdSubject);
    }

    @Test
    void givenSubjectWithIncorrectCourse_whenSave_thenWasThrownCourseNumberOutOfBoundsException() {
        ReflectionTestUtils.setField(subjectService, "maxCourse", 0);

        assertThrows(CourseNumberOutOfBoundsException.class, () -> subjectService.save(createdSubject), "Course out of bounds");
        verify(subjectDao, never()).save(createdSubject);
    }

    @Test
    void givenSubjectWithNonExistentId_whenUpdate_thenWasThrownEntityNotFoundException() {
        ReflectionTestUtils.setField(subjectService, "maxCourse", 6);
        given(subjectDao.getById(anyInt())).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> subjectService.update(updatedSubject), "Subject is not present");
        verify(subjectDao, never()).update(updatedSubject);
    }

    @Test
    void givenSubjectWithIncorrectCourse_whenUpdate_thenWasThrownCourseNumberOutOfBoundsException() {
        ReflectionTestUtils.setField(subjectService, "maxCourse", 0);
        given(subjectDao.getById(anyInt())).willReturn(Optional.of(retrievedSubject));

        assertThrows(CourseNumberOutOfBoundsException.class, () -> subjectService.update(updatedSubject), "Course out of bounds");
        verify(subjectDao, never()).update(updatedSubject);
    }

    @Test
    void givenDataThatProducesEmptyReturn_whenGetAllByTeacherId_thenCalledSubjectDaoGetAllByTeacherIdAndReturnedAllSubjectsOfGivenTeacher() {
        given(subjectDao.getAllByTeacherId(anyInt())).willReturn(emptyList());

        List<Subject> actualSubjects = subjectService.getAllByTeacherId(1);

        verify(subjectDao, times(1)).getAllByTeacherId(1);
        assertEquals(emptyList(), actualSubjects);
    }
}