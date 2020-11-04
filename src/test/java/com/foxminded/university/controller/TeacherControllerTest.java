package com.foxminded.university.controller;

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

import java.util.Optional;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        when(teacherService.findAll()).thenReturn(singletonList(retrievedTeacher));

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("teachers/teachers"))
                .andExpect(model().attribute("teachers", is(singletonList(retrievedTeacher))));
    }

    @Test
    void whenRedirectToSaveForm_thenRedirectedToAddingForm() throws Exception {
        when(subjectService.findAll()).thenReturn(singletonList(retrievedSubject));

        mockMvc.perform(get("/teachers/new"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("teachers/new"));
    }

    @Test
    void whenEdit_thenAddedTeacherModelWithGivenIdAndRedirectedToFilledEditingForm() throws Exception {
        when(teacherService.findById(1)).thenReturn(Optional.of(retrievedTeacher));
        when(subjectService.findAll()).thenReturn(singletonList(retrievedSubject));

        mockMvc.perform(get("/teachers/edit/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("teachers/edit"))
                .andExpect(model().attribute("teacher", is(retrievedTeacher)));
    }

    @Test
    void whenShowSubjects_thenAddedModelWithSubjectsListOfTeacherWithGivenIdAndRedirectedToSubjectsViewingPage() throws Exception {
        when(teacherService.findById(1)).thenReturn(Optional.of(retrievedTeacher));

        mockMvc.perform(get("/teachers/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("teachers/subjects"))
                .andExpect(model().attribute("subjects", is(singletonList(retrievedSubject))));
    }

    @Test
    void whenDelete_thenCalledTeacherServiceDeleteWithGivenIdAndRedirectedToPageWithListOfTeachers() throws Exception {
        mockMvc.perform(get("/teachers/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers"));

        verify(teacherService, times(1)).delete(1);
    }

    @Test
    void whenSave_thenCalledTeacherServiceSaveWithGivenTeacherAndRedirectedToPageWithListOfTeachers() throws Exception {
        mockMvc.perform(post("/teachers/save")
                .flashAttr("teacher", retrievedTeacher))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers"));

        verify(teacherService, times(1)).save(retrievedTeacher);
    }

    @Test
    void whenUpdate_thenCalledTeacherServiceSaveWithGivenTeacherAndRedirectedToPageWithListOfTeachers() throws Exception {
        mockMvc.perform(post("/teachers/update/1")
                .flashAttr("teacher", updatedTeacher))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers"));

        verify(teacherService, times(1)).update(updatedTeacher);
    }
}