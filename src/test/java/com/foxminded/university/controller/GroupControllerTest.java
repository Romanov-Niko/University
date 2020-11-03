package com.foxminded.university.controller;

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

import java.util.Optional;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void whenShowAll_thenAddedModelWithAllGroupsAndRedirectedToFormWithListOfGroups() throws Exception {
        when(groupService.findAll()).thenReturn(singletonList(retrievedGroup));

        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("groups/groups"))
                .andExpect(model().attribute("groups", is(singletonList(retrievedGroup))));
    }

    @Test
    void whenRedirectToSaveForm_thenRedirectedToAddingForm() throws Exception {
        when(studentService.findAll()).thenReturn(singletonList(retrievedStudent));

        mockMvc.perform(get("/groups/new"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("groups/new"));
    }

    @Test
    void whenEdit_thenAddedGroupModelWithGivenIdAndRedirectedToFilledEditingForm() throws Exception {
        when(groupService.findById(1)).thenReturn(Optional.of(retrievedGroup));
        when(studentService.findAll()).thenReturn(singletonList(retrievedStudent));

        mockMvc.perform(get("/groups/edit/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("groups/edit"))
                .andExpect(model().attribute("group", is(retrievedGroup)))
                .andExpect(model().attribute("allStudents", is(singletonList(retrievedStudent))));
    }

    @Test
    void whenShowStudents_thenAddedModelWithStudentsListOfGroupWithGivenIdAndRedirectedToStudentsViewingPage() throws Exception {
        when(groupService.findById(1)).thenReturn(Optional.of(retrievedGroup));

        mockMvc.perform(get("/groups/students/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("groups/students"))
                .andExpect(model().attribute("students", is(singletonList(retrievedStudent))));
    }

    @Test
    void whenDelete_thenCalledGroupServiceDeleteWithGivenIdAndRedirectedToPageWithListOfGroups() throws Exception {
        mockMvc.perform(get("/groups/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"));

        verify(groupService, times(1)).delete(1);
    }

    @Test
    void whenSave_thenCalledGroupServiceSaveWithGivenGroupAndRedirectedToPageWithListOfGroups() throws Exception {
        mockMvc.perform(post("/groups/save")
                .flashAttr("group", retrievedGroup))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"));

        verify(groupService, times(1)).save(retrievedGroup);
    }

    @Test
    void whenUpdate_thenCalledGroupServiceSaveWithGivenGroupAndRedirectedToPageWithListOfGroups() throws Exception {
        mockMvc.perform(post("/groups/update/1")
                .flashAttr("group", updatedGroup))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"));

        verify(groupService, times(1)).update(updatedGroup);
    }
}