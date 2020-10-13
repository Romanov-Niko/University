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

import java.time.LocalDate;
import java.util.Optional;

import static com.foxminded.university.TestData.retrievedSubject;
import static com.foxminded.university.TestData.retrievedTeacher;
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
class SubjectControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private SubjectController subjectController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController).build();
    }

    @Test
    void whenShowAll_thenAddedModelWithAllSubjectsAndRedirectedToFormWithListOfSubjects() throws Exception {
        when(subjectService.getAll()).thenReturn(singletonList(retrievedSubject));

        mockMvc.perform(get("/subjects"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("subjects/subjects"))
                .andExpect(model().attribute("subjects", hasSize(1)))
                .andExpect(model().attribute("subjects", is(singletonList(retrievedSubject))));

        verify(subjectService, times(1)).getAll();
    }

    @Test
    void whenRedirectToSaveForm_thenAddedEmptySubjectModelAndRedirectedToAddingForm() throws Exception {
        mockMvc.perform(get("/subjects/new"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("subjects/new"))
                .andExpect(model().attribute("subject", hasProperty("id", is(0))))
                .andExpect(model().attribute("subject", hasProperty("name", is(nullValue()))))
                .andExpect(model().attribute("subject", hasProperty("creditHours", is(nullValue()))))
                .andExpect(model().attribute("subject", hasProperty("course", is(nullValue()))))
                .andExpect(model().attribute("subject", hasProperty("specialty", is(nullValue()))));
    }

    @Test
    void whenEdit_thenAddedSubjectModelWithGivenIdAndRedirectedToFilledEditingForm() throws Exception {
        when(subjectService.getById(anyInt())).thenReturn(Optional.of(retrievedSubject));

        mockMvc.perform(get("/subjects/edit/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("subjects/edit"))
                .andExpect(model().attribute("subject", is(retrievedSubject)));

        verify(subjectService, times(1)).getById(anyInt());
    }
}