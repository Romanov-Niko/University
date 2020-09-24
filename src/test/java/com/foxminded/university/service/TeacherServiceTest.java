package com.foxminded.university.service;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import com.foxminded.university.domain.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.foxminded.university.TestData.createdGroup;
import static com.foxminded.university.TestData.updatedGroup;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

import static com.foxminded.university.TestData.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherDao teacherDao;

    @Mock
    private SubjectDao subjectDao;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void givenNothing_whenGetAll_thenCalledTeacherDaoGetAllAndReturnedAllTeachers() {
        given(teacherDao.getAll()).willReturn(singletonList(retrievedTeacher));

        List<Teacher> actualTeachers = teacherService.getAll();

        verify(teacherDao, times(1)).getAll();
        assertEquals(singletonList(retrievedTeacher), actualTeachers);
    }

    @Test
    void givenTeacher_whenSave_thenCalledTeacherDaoSave() {
        given(subjectDao.getById(anyInt())).willReturn(Optional.of(retrievedSubject));

        teacherService.save(createdTeacher);

        verify(teacherDao, times(1)).save(createdTeacher);
    }

    @Test
    void givenTeacher_whenUpdate_thenCalledTeacherDaoUpdate() {
        given(subjectDao.getById(anyInt())).willReturn(Optional.of(retrievedSubject));
        given(teacherDao.getById(anyInt())).willReturn(Optional.of(updatedTeacher));

        teacherService.update(updatedTeacher);

        verify(teacherDao, times(1)).update(updatedTeacher);
    }

    @Test
    void givenTeacherId_whenDelete_thenCalledTeacherDaoDelete() {
        teacherService.delete(1);

        verify(teacherDao, times(1)).delete(1);
    }

    @Test
    void givenEmptyTable_whenGetAll_thenCalledTeacherDaoGetAllAndReturnedEmptyList() {
        given(teacherDao.getAll()).willReturn(emptyList());

        List<Teacher> actualTeachers = teacherService.getAll();

        verify(teacherDao, times(1)).getAll();
        assertEquals(emptyList(), actualTeachers);
    }

    @Test
    void givenNonExistentSubjectId_whenSave_thenWasNotCalledTeacherDaoSave() {
        given(subjectDao.getById(anyInt())).willReturn(Optional.empty());

        teacherService.save(createdTeacher);

        verify(teacherDao, never()).save(createdTeacher);
    }

    @Test
    void givenNonExistentTeacherId_whenUpdate_thenWasNotCalledTeacherDaoUpdate() {
        given(teacherDao.getById(anyInt())).willReturn(Optional.empty());

        teacherService.update(updatedTeacher);

        verify(teacherDao, never()).update(updatedTeacher);
    }

    @Test
    void givenNonExistentSubjectId_whenUpdate_thenWasNotCalledTeacherDaoUpdate() {
        teacherService.update(updatedTeacher);

        verify(teacherDao, never()).update(updatedTeacher);
    }
}