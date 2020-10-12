package com.foxminded.university.controller;

import com.foxminded.university.service.AudienceService;
import com.foxminded.university.service.GroupService;
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

import static com.foxminded.university.TestData.*;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GroupService groupService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private GroupController groupController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    void showAll() throws Exception {
        when(groupService.getAll()).thenReturn(singletonList(retrievedGroup));

        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("groups/groups"))
                .andExpect(model().attribute("groups", hasSize(1)))
                .andExpect(model().attribute("groups", hasItem(
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("name", is("AA-11"))
                        )
                )));

        verify(groupService, times(1)).getAll();
    }

    @Test
    void redirectToSaveForm() throws Exception {
        when(studentService.getAll()).thenReturn(singletonList(retrievedStudent));

        mockMvc.perform(get("/groups/new"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("groups/new"))
                .andExpect(model().attribute("group", hasProperty("id", is(0))))
                .andExpect(model().attribute("group", hasProperty("name", is(nullValue()))));

        verify(studentService, times(1)).getAll();
    }

    @Test
    void edit() throws Exception {
        when(groupService.getById(anyInt())).thenReturn(Optional.of(retrievedGroup));
        when(studentService.getAll()).thenReturn(singletonList(retrievedStudent));

        mockMvc.perform(get("/groups/edit/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("groups/edit"))
                .andExpect(model().attribute("group", hasProperty("id", is(1))))
                .andExpect(model().attribute("group", hasProperty("name", is("AA-11"))))
                .andExpect(model().attribute("allStudents", is(singletonList(retrievedStudent))));

        verify(groupService, times(1)).getById(anyInt());
        verify(studentService, times(1)).getAll();
    }

    @Test
    void showSubjects() throws Exception {
        when(groupService.getById(anyInt())).thenReturn(Optional.of(retrievedGroup));

        mockMvc.perform(get("/groups/students/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("groups/students"))
                .andExpect(model().attribute("students", is(singletonList(retrievedStudent))));

        verify(groupService, times(1)).getById(anyInt());
    }
}