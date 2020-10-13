package com.foxminded.university.controller;

import com.foxminded.university.service.AudienceService;
import com.foxminded.university.service.StudentService;
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

import static com.foxminded.university.TestData.retrievedAudience;
import static com.foxminded.university.TestData.retrievedStudent;
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
class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void whenShowAll_thenAddedModelWithAllStudentsAndRedirectedToFormWithListOfStudents() throws Exception {
        when(studentService.getAll()).thenReturn(singletonList(retrievedStudent));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("students/students"))
                .andExpect(model().attribute("students", hasSize(1)))
                .andExpect(model().attribute("students", is(singletonList(retrievedStudent))));

        verify(studentService, times(1)).getAll();
    }

    @Test
    void whenRedirectToSaveForm_thenAddedEmptyStudentModelAndRedirectedToAddingForm() throws Exception {
        mockMvc.perform(get("/students/new"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("students/new"))
                .andExpect(model().attribute("student", hasProperty("name", is(nullValue()))))
                .andExpect(model().attribute("student", hasProperty("surname", is(nullValue()))))
                .andExpect(model().attribute("student", hasProperty("dateOfBirth", is(nullValue()))))
                .andExpect(model().attribute("student", hasProperty("gender", is(nullValue()))))
                .andExpect(model().attribute("student", hasProperty("email", is(nullValue()))))
                .andExpect(model().attribute("student", hasProperty("phoneNumber", is(nullValue()))))
                .andExpect(model().attribute("student", hasProperty("id", is(0))))
                .andExpect(model().attribute("student", hasProperty("groupId", is(0))))
                .andExpect(model().attribute("student", hasProperty("specialty", is(nullValue()))))
                .andExpect(model().attribute("student", hasProperty("course", is(nullValue()))))
                .andExpect(model().attribute("student", hasProperty("admission", is(nullValue()))))
                .andExpect(model().attribute("student", hasProperty("graduation", is(nullValue()))));
    }

    @Test
    void whenEdit_thenAddedStudentModelWithGivenIdAndRedirectedToFilledEditingForm() throws Exception {
        when(studentService.getById(anyInt())).thenReturn(Optional.of(retrievedStudent));

        mockMvc.perform(get("/students/edit/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("students/edit"))
                .andExpect(model().attribute("student", is(retrievedStudent)));

        verify(studentService, times(1)).getById(anyInt());
    }
}