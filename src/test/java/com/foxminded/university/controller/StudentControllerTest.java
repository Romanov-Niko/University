package com.foxminded.university.controller;

import com.foxminded.university.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.foxminded.university.TestData.retrievedStudent;
import static com.foxminded.university.TestData.updatedStudent;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        when(studentService.findAll()).thenReturn(singletonList(retrievedStudent));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("students/students"))
                .andExpect(model().attribute("students", is(singletonList(retrievedStudent))));
    }

    @Test
    void whenRedirectToSaveForm_thenRedirectedToAddingForm() throws Exception {
        mockMvc.perform(get("/students/new"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("students/new"));
    }

    @Test
    void whenEdit_thenAddedStudentModelWithGivenIdAndRedirectedToFilledEditingForm() throws Exception {
        when(studentService.findById(1)).thenReturn(Optional.of(retrievedStudent));

        mockMvc.perform(get("/students/edit/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("students/edit"))
                .andExpect(model().attribute("student", is(retrievedStudent)));
    }

    @Test
    void whenDelete_thenCalledStudentServiceDeleteWithGivenIdAndRedirectedToPageWithListOfStudents() throws Exception {
        mockMvc.perform(get("/students/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentService, times(1)).delete(1);
    }

    @Test
    void whenSave_thenCalledStudentServiceSaveWithGivenStudentAndRedirectedToPageWithListOfStudents() throws Exception {
        mockMvc.perform(post("/students/save")
                .flashAttr("student", retrievedStudent))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentService, times(1)).save(retrievedStudent);
    }

    @Test
    void whenUpdate_thenCalledStudentServiceSaveWithGivenStudentAndRedirectedToPageWithListOfStudents() throws Exception {
        mockMvc.perform(post("/students/update/1")
                .flashAttr("student", updatedStudent))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentService, times(1)).update(updatedStudent);
    }
}