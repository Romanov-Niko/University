package com.foxminded.university.controller;

import com.foxminded.university.domain.Subject;
import com.foxminded.university.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static com.foxminded.university.TestData.*;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@ExtendWith(MockitoExtension.class)
class LessonControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SubjectService subjectService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private LessonTimeService lessonTimeService;

    @Mock
    private GroupService groupService;

    @Mock
    private AudienceService audienceService;

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private LessonController lessonController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController).build();
    }

    @Test
    void whenShowAll_thenAddedModelWithAllLessonsAndRedirectedToFormWithListOfLessons() throws Exception {
        when(lessonService.getAll()).thenReturn(singletonList(retrievedLesson));

        mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("lessons/lessons"))
                .andExpect(model().attribute("lessons", is(singletonList(retrievedLesson))));
    }

    @Test
    void whenRedirectToSaveForm_thenRedirectedToAddingForm() throws Exception {
        when(audienceService.getAll()).thenReturn(singletonList(retrievedAudience));
        when(subjectService.getAll()).thenReturn(singletonList(retrievedSubject));
        when(teacherService.getAll()).thenReturn(singletonList(retrievedTeacher));
        when(lessonTimeService.getAll()).thenReturn(singletonList(retrievedLessonTime));
        when(groupService.getAll()).thenReturn(singletonList(retrievedGroup));

        mockMvc.perform(get("/lessons/new"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("lessons/new"));
    }

    @Test
    void whenEdit_thenAddedLessonModelWithGivenIdAndRedirectedToFilledEditingForm() throws Exception {
        when(lessonService.getById(1)).thenReturn(Optional.of(retrievedLesson));
        when(audienceService.getAll()).thenReturn(singletonList(retrievedAudience));
        when(subjectService.getAll()).thenReturn(singletonList(retrievedSubject));
        when(teacherService.getAll()).thenReturn(singletonList(retrievedTeacher));
        when(lessonTimeService.getAll()).thenReturn(singletonList(retrievedLessonTime));
        when(groupService.getAll()).thenReturn(singletonList(retrievedGroup));

        mockMvc.perform(get("/lessons/edit/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("lessons/edit"))
                .andExpect(model().attribute("lesson", is(retrievedLesson)));
    }

    @Test
    void whenShowGroups_thenAddedModelWithGroupsListOfLessonWithGivenIdAndRedirectedToGroupsViewingPage() throws Exception {
        when(lessonService.getById(1)).thenReturn(Optional.of(retrievedLesson));

        mockMvc.perform(get("/lessons/groups/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("lessons/groups"))
                .andExpect(model().attribute("groups", is(singletonList(retrievedGroup))));
    }

    @Test
    void whenDelete_thenCalledLessonServiceDeleteWithGivenIdAndRedirectedToPageWithListOfLessons() throws Exception {
        mockMvc.perform(get("/lessons/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lessons"));

        verify(lessonService, times(1)).delete(1);
    }

    @Test
    void whenSave_thenCalledLessonServiceSaveWithGivenLessonAndRedirectedToPageWithListOfLessons() throws Exception {
        mockMvc.perform(post("/lessons/save")
                .flashAttr("lesson", retrievedLesson))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lessons"));

        verify(lessonService, times(1)).save(retrievedLesson);
    }

    @Test
    void whenUpdate_thenCalledLessonServiceSaveWithGivenLessonAndRedirectedToPageWithListOfLessons() throws Exception {
        mockMvc.perform(post("/lessons/update/1")
                .flashAttr("lesson", updatedLesson))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lessons"));

        verify(lessonService, times(1)).update(updatedLesson);
    }
}