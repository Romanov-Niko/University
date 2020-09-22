package com.foxminded.university.service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.LessonTimeDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.domain.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

import static com.foxminded.university.TestData.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDao studentDao;

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

        studentService.save(createdStudent);

        verify(studentDao, times(1)).save(createdStudent);
    }

    @Test
    void givenStudent_whenUpdate_thenCalledStudentDaoUpdate() {
        ReflectionTestUtils.setField(studentService, "maxCourse", 6);
        given(studentDao.getById(anyInt())).willReturn(Optional.of(retrievedStudent));

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
}