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
    void givenLessonsUrl_whenShowAll_thenReturnedLessonsHtmlAndModelWithAllLessons() throws Exception {
        when(lessonService.getAll()).thenReturn(singletonList(retrievedLesson));

        mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("lessons/lessons"))
                .andExpect(model().attribute("lessons", hasSize(1)))
                .andExpect(model().attribute("lessons", hasItem(
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("subject", is(retrievedSubject)),
                                hasProperty("teacher", is(retrievedTeacher)),
                                hasProperty("audience", is(retrievedAudience)),
                                hasProperty("lessonTime", is(retrievedLessonTime)),
                                hasProperty("date", is(LocalDate.parse("2017-06-01")))
                        )
                )));

        verify(lessonService, times(1)).getAll();
    }

    @Test
    void givenLessonsNewUrl_whenRedirectToSaveForm_thenReturnedNewHtmlAndEmptyLessonModel() throws Exception {
        when(audienceService.getAll()).thenReturn(singletonList(retrievedAudience));
        when(subjectService.getAll()).thenReturn(singletonList(retrievedSubject));
        when(teacherService.getAll()).thenReturn(singletonList(retrievedTeacher));
        when(lessonTimeService.getAll()).thenReturn(singletonList(retrievedLessonTime));
        when(groupService.getAll()).thenReturn(singletonList(retrievedGroup));

        mockMvc.perform(get("/lessons/new"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("lessons/new"))
                .andExpect(model().attribute("lesson", hasProperty("id", is(0))))
                .andExpect(model().attribute("lesson", hasProperty("subject", is(nullValue()))))
                .andExpect(model().attribute("lesson", hasProperty("teacher", is(nullValue()))))
                .andExpect(model().attribute("lesson", hasProperty("audience", is(nullValue()))))
                .andExpect(model().attribute("lesson", hasProperty("lessonTime", is(nullValue()))))
                .andExpect(model().attribute("lesson", hasProperty("date", is(nullValue()))));
    }

    @Test
    void givenLessonsEditUrl_whenEdit_thenReturnedEditHtmlAndLessonModelWithGivenId() throws Exception {
        when(lessonService.getById(anyInt())).thenReturn(Optional.of(retrievedLesson));
        when(audienceService.getAll()).thenReturn(singletonList(retrievedAudience));
        when(subjectService.getAll()).thenReturn(singletonList(retrievedSubject));
        when(teacherService.getAll()).thenReturn(singletonList(retrievedTeacher));
        when(lessonTimeService.getAll()).thenReturn(singletonList(retrievedLessonTime));
        when(groupService.getAll()).thenReturn(singletonList(retrievedGroup));

        mockMvc.perform(get("/lessons/edit/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("lessons/edit"))
                .andExpect(model().attribute("lesson", hasProperty("id", is(1))))
                .andExpect(model().attribute("lesson", hasProperty("subject", is(retrievedSubject))))
                .andExpect(model().attribute("lesson", hasProperty("teacher", is(retrievedTeacher))))
                .andExpect(model().attribute("lesson", hasProperty("audience", is(retrievedAudience))))
                .andExpect(model().attribute("lesson", hasProperty("lessonTime", is(retrievedLessonTime))))
                .andExpect(model().attribute("lesson", hasProperty("date", is(LocalDate.parse("2017-06-01")))));

        verify(lessonService, times(1)).getById(anyInt());
    }

    @Test
    void givenLessonsGroupsUrl_whenShowGroups_thenReturnedSubjectsHtmlAndModelWithListOfGroupsOfGivenLessonId() throws Exception {
        when(lessonService.getById(anyInt())).thenReturn(Optional.of(retrievedLesson));

        mockMvc.perform(get("/lessons/groups/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("lessons/groups"))
                .andExpect(model().attribute("groups", is(singletonList(retrievedGroup))));

        verify(lessonService, times(1)).getById(anyInt());
    }
}