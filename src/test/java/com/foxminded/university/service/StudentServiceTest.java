package com.foxminded.university.service;

import com.foxminded.university.repository.GroupRepository;
import com.foxminded.university.repository.StudentRepository;
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

import static com.foxminded.university.TestData.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void givenNothing_whenGetAll_thenCalledStudentDaoGetAllAndReturnedAllStudents() {
        given(studentRepository.findAll()).willReturn(singletonList(retrievedStudent));

        List<Student> actualStudents = studentService.findAll();

        verify(studentRepository, times(1)).findAll();
        assertEquals(singletonList(retrievedStudent), actualStudents);
    }

    @Test
    void givenStudent_whenSave_thenCalledStudentDaoSave() {
        ReflectionTestUtils.setField(studentService, "maxCourse", 6);
        given(groupRepository.findById(1)).willReturn(Optional.of(retrievedGroup));

        studentService.save(createdStudent);

        verify(studentRepository, times(1)).save(createdStudent);
    }

    @Test
    void givenStudent_whenUpdate_thenCalledStudentDaoUpdate() {
        ReflectionTestUtils.setField(studentService, "maxCourse", 6);
        given(studentRepository.findById(1)).willReturn(Optional.of(retrievedStudent));
        given(groupRepository.findById(1)).willReturn(Optional.of(retrievedGroup));

        studentService.update(updatedStudent);

        verify(studentRepository, times(1)).save(updatedStudent);
    }

    @Test
    void givenStudentId_whenDelete_thenCalledStudentDaoDelete() {
        studentService.delete(1);

        verify(studentRepository, times(1)).deleteById(1);
    }

    @Test
    void givenGroupId_whenGetAllByGroupId_thenCalledStudentDaoGetAllByGroupIdAndReturnedStudentsOfGivenGroup() {
        given(studentRepository.findAllByGroupId(1)).willReturn(singletonList(retrievedStudent));

        List<Student> actualStudents = studentService.findAllByGroupId(1);

        verify(studentRepository, times(1)).findAllByGroupId(1);
        assertEquals(singletonList(retrievedStudent), actualStudents);
    }

    @Test
    void givenGroupName_whenGetAllByGroupName_thenCalledStudentDaoAndReturnedStudentsOfGivenGroup() {
        given(studentRepository.findAllByGroupName("AA-11")).willReturn(singletonList(retrievedStudent));

        List<Student> actualStudents = studentService.findAllByGroupName("AA-11");

        verify(studentRepository, times(1)).findAllByGroupName(retrievedGroup.getName());
        assertEquals(singletonList(retrievedStudent), actualStudents);
    }

    @Test
    void givenEmptyTable_whenGetAll_thenCalledStudentDaoGetAllAndReturnedEmptyList() {
        given(studentRepository.findAll()).willReturn(emptyList());

        List<Student> actualStudents = studentService.findAll();

        verify(studentRepository, times(1)).findAll();
        assertEquals(emptyList(), actualStudents);
    }

    @Test
    void givenStudentWithIncorrectCourse_whenSave_thenCourseNumberOutOfBoundsExceptionThrown() {
        ReflectionTestUtils.setField(studentService, "maxCourse", 0);

        Throwable exception = assertThrows(CourseNumberOutOfBoundsException.class, () -> studentService.save(createdStudent));
        assertEquals("Course number is out of bounds", exception.getMessage());
        verify(studentRepository, never()).save(createdStudent);
    }

    @Test
    void givenStudentWithNonExistentId_whenUpdate_thenEntityNotFoundExceptionThrown() {
        ReflectionTestUtils.setField(studentService, "maxCourse", 6);
        given(studentRepository.findById(1)).willReturn(Optional.empty());

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> studentService.update(updatedStudent));
        assertEquals("Student with id 1 is not present", exception.getMessage());
        verify(studentRepository, never()).save(updatedStudent);
    }

    @Test
    void givenStudentWithIncorrectCourse_whenUpdate_thenCourseNumberOutOfBoundsExceptionThrown() {
        ReflectionTestUtils.setField(studentService, "maxCourse", 0);
        given(studentRepository.findById(1)).willReturn(Optional.of(retrievedStudent));

        Throwable exception = assertThrows(CourseNumberOutOfBoundsException.class, () -> studentService.update(updatedStudent));
        assertEquals("Course number is out of bounds", exception.getMessage());
        verify(studentRepository, never()).save(updatedStudent);
    }

    @Test
    void givenDataThatProducesEmptyReturn_whenGetAllByGroupId_thenCalledStudentDaoGetAllByGroupIdAndReturnedEmptyList() {
        given(studentRepository.findAllByGroupId(1)).willReturn(emptyList());

        List<Student> actualStudents = studentService.findAllByGroupId(1);

        verify(studentRepository, times(1)).findAllByGroupId(1);
        assertEquals(emptyList(), actualStudents);
    }

    @Test
    void givenDataThatProducesEmptyReturn_whenGetAllByGroupName_thenCalledStudentDaoAndReturnedEmptyList() {
        given(studentRepository.findAllByGroupName("AA-11")).willReturn(emptyList());

        List<Student> actualStudents = studentService.findAllByGroupName("AA-11");

        verify(studentRepository, times(1)).findAllByGroupName(retrievedGroup.getName());
        assertEquals(emptyList(), actualStudents);
    }
}