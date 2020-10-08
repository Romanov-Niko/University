package com.foxminded.university.service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.domain.Student;
import com.foxminded.university.exception.CourseNumberOutOfBoundsException;
import com.foxminded.university.exception.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

import static com.foxminded.university.TestData.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDao studentDao;

    @Mock
    private GroupDao groupDao;

    @InjectMocks
    private StudentService studentService;

    @Test
    void givenNothing_whenGetAll_thenCalledStudentDaoGetAllAndReturnedAllStudents() {
        given(studentDao.getAll()).willReturn(singletonList(retrievedStudent));

        List<Student> actualStudents = studentService.getAll();

        verify(studentDao, times(1)).getAll();
        assertEquals(singletonList(retrievedStudent), actualStudents);
    }

    @Test
    void givenStudent_whenSave_thenCalledStudentDaoSave() {
        ReflectionTestUtils.setField(studentService, "maxCourse", 6);
        given(groupDao.getById(anyInt())).willReturn(Optional.of(retrievedGroup));

        studentService.save(createdStudent);

        verify(studentDao, times(1)).save(createdStudent);
    }

    @Test
    void givenStudent_whenUpdate_thenCalledStudentDaoUpdate() {
        ReflectionTestUtils.setField(studentService, "maxCourse", 6);
        given(studentDao.getById(anyInt())).willReturn(Optional.of(retrievedStudent));
        given(groupDao.getById(anyInt())).willReturn(Optional.of(retrievedGroup));

        studentService.update(updatedStudent);

        verify(studentDao, times(1)).update(updatedStudent);
    }

    @Test
    void givenStudentId_whenDelete_thenCalledStudentDaoDelete() {
        studentService.delete(1);

        verify(studentDao, times(1)).delete(1);
    }

    @Test
    void givenGroupId_whenGetAllByGroupId_thenCalledStudentDaoGetAllByGroupIdAndReturnedStudentsOfGivenGroup() {
        given(studentDao.getAllByGroupId(anyInt())).willReturn(singletonList(retrievedStudent));

        List<Student> actualStudents = studentService.getAllByGroupId(1);

        verify(studentDao, times(1)).getAllByGroupId(1);
        assertEquals(singletonList(retrievedStudent), actualStudents);
    }

    @Test
    void givenGroupName_whenGetAllByGroupName_thenCalledStudentDaoAndReturnedStudentsOfGivenGroup() {
        given(studentDao.getAllByGroupName(anyString())).willReturn(singletonList(retrievedStudent));

        List<Student> actualStudents = studentService.getAllByGroupName("AA-11");

        verify(studentDao, times(1)).getAllByGroupName(retrievedGroup.getName());
        assertEquals(singletonList(retrievedStudent), actualStudents);
    }

    @Test
    void givenEmptyTable_whenGetAll_thenCalledStudentDaoGetAllAndReturnedEmptyList() {
        given(studentDao.getAll()).willReturn(emptyList());

        List<Student> actualStudents = studentService.getAll();

        verify(studentDao, times(1)).getAll();
        assertEquals(emptyList(), actualStudents);
    }

    @Test
    void givenStudentWithIncorrectCourse_whenSave_thenCourseNumberOutOfBoundsExceptionThrown() {
        ReflectionTestUtils.setField(studentService, "maxCourse", 0);

        Throwable exception = assertThrows(CourseNumberOutOfBoundsException.class, () -> studentService.save(createdStudent));
        assertEquals("Course number is out of bounds", exception.getMessage());
        verify(studentDao, never()).save(createdStudent);
    }

    @Test
    void givenStudentWithNonExistentId_whenUpdate_thenEntityNotFoundExceptionThrown() {
        ReflectionTestUtils.setField(studentService, "maxCourse", 6);
        given(studentDao.getById(anyInt())).willReturn(Optional.empty());

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> studentService.update(updatedStudent));
        assertEquals("Student with id 1 is not present", exception.getMessage());
        verify(studentDao, never()).update(updatedStudent);
    }

    @Test
    void givenStudentWithIncorrectCourse_whenUpdate_thenCourseNumberOutOfBoundsExceptionThrown() {
        ReflectionTestUtils.setField(studentService, "maxCourse", 0);
        given(studentDao.getById(anyInt())).willReturn(Optional.of(retrievedStudent));

        Throwable exception = assertThrows(CourseNumberOutOfBoundsException.class, () -> studentService.update(updatedStudent));
        assertEquals("Course number is out of bounds", exception.getMessage());
        verify(studentDao, never()).update(updatedStudent);
    }

    @Test
    void givenDataThatProducesEmptyReturn_whenGetAllByGroupId_thenCalledStudentDaoGetAllByGroupIdAndReturnedEmptyList() {
        given(studentDao.getAllByGroupId(anyInt())).willReturn(emptyList());

        List<Student> actualStudents = studentService.getAllByGroupId(1);

        verify(studentDao, times(1)).getAllByGroupId(1);
        assertEquals(emptyList(), actualStudents);
    }

    @Test
    void givenDataThatProducesEmptyReturn_whenGetAllByGroupName_thenCalledStudentDaoAndReturnedEmptyList() {
        given(studentDao.getAllByGroupName(anyString())).willReturn(emptyList());

        List<Student> actualStudents = studentService.getAllByGroupName("AA-11");

        verify(studentDao, times(1)).getAllByGroupName(retrievedGroup.getName());
        assertEquals(emptyList(), actualStudents);
    }
}