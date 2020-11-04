package com.foxminded.university.controller;

import com.foxminded.university.service.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.foxminded.university.TestData.retrievedSubject;
import static com.foxminded.university.TestData.updatedSubject;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        when(subjectService.findAll()).thenReturn(singletonList(retrievedSubject));

        mockMvc.perform(get("/subjects"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("subjects/subjects"))
                .andExpect(model().attribute("subjects", is(singletonList(retrievedSubject))));
    }

    @Test
    void whenRedirectToSaveForm_thenRedirectedToAddingForm() throws Exception {
        mockMvc.perform(get("/subjects/new"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("subjects/new"));
    }

    @Test
    void whenEdit_thenAddedSubjectModelWithGivenIdAndRedirectedToFilledEditingForm() throws Exception {
        when(subjectService.findById(1)).thenReturn(Optional.of(retrievedSubject));

        mockMvc.perform(get("/subjects/edit/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("subjects/edit"))
                .andExpect(model().attribute("subject", is(retrievedSubject)));
    }

    @Test
    void whenDelete_thenCalledSubjectServiceDeleteWithGivenIdAndRedirectedToPageWithListOfSubjects() throws Exception {
        mockMvc.perform(get("/subjects/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subjects"));

        verify(subjectService, times(1)).delete(1);
    }

    @Test
    void whenSave_thenCalledSubjectServiceSaveWithGivenSubjectAndRedirectedToPageWithListOfSubjects() throws Exception {
        mockMvc.perform(post("/subjects/save")
                .flashAttr("subject", retrievedSubject))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subjects"));

        verify(subjectService, times(1)).save(retrievedSubject);
    }

    @Test
    void whenUpdate_thenCalledSubjectServiceSaveWithGivenSubjectAndRedirectedToPageWithListOfSubjects() throws Exception {
        mockMvc.perform(post("/subjects/update/1")
                .flashAttr("subject", updatedSubject))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subjects"));

        verify(subjectService, times(1)).update(updatedSubject);
    }
}