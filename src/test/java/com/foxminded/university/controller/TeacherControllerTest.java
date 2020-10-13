package com.foxminded.university.controller;

import com.foxminded.university.domain.Subject;
import com.foxminded.university.service.AudienceService;
import com.foxminded.university.service.SubjectService;
import com.foxminded.university.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TeacherService teacherService;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    void whenShowAll_thenAddedModelWithAllTeachersAndRedirectedToFormWithListOfTeachers() throws Exception {
        when(teacherService.getAll()).thenReturn(singletonList(retrievedTeacher));

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("teachers/teachers"))
                .andExpect(model().attribute("teachers", hasSize(1)))
                .andExpect(model().attribute("teachers", is(singletonList(retrievedTeacher))));

        verify(teacherService, times(1)).getAll();
    }

    @Test
    void whenRedirectToSaveForm_thenRedirectedToAddingForm() throws Exception {
        when(subjectService.getAll()).thenReturn(singletonList(retrievedSubject));

        mockMvc.perform(get("/teachers/new"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("teachers/new"));

        verify(subjectService, times(1)).getAll();
    }

    @Test
    void whenEdit_thenAddedTeacherModelWithGivenIdAndRedirectedToFilledEditingForm() throws Exception {
        when(teacherService.getById(anyInt())).thenReturn(Optional.of(retrievedTeacher));
        when(subjectService.getAll()).thenReturn(singletonList(retrievedSubject));

        mockMvc.perform(get("/teachers/edit/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("teachers/edit"))
                .andExpect(model().attribute("teacher", is(retrievedTeacher)));

        verify(teacherService, times(1)).getById(anyInt());
        verify(subjectService, times(1)).getAll();
    }

    @Test
    void whenShowSubjects_thenAddedModelWithSubjectsListOfTeacherWithGivenIdAndRedirectedToSubjectsViewingPage() throws Exception {
        when(teacherService.getById(anyInt())).thenReturn(Optional.of(retrievedTeacher));

        mockMvc.perform(get("/teachers/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("teachers/subjects"))
                .andExpect(model().attribute("subjects", is(singletonList(retrievedSubject))));

        verify(teacherService, times(1)).getById(anyInt());
    }
}