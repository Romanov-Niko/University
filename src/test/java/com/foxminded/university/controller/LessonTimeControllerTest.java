package com.foxminded.university.controller;

import com.foxminded.university.service.LessonTimeService;
import com.foxminded.university.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static com.foxminded.university.TestData.retrievedAudience;
import static com.foxminded.university.TestData.retrievedLessonTime;
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
class LessonTimeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LessonTimeService lessonTimeService;

    @InjectMocks
    private LessonTimeController lessonTimeController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(lessonTimeController).build();
    }

    @Test
    void whenShowAll_thenAddedModelWithAllLessonsTimesAndRedirectedToFormWithListOfLessonsTimes() throws Exception {
        when(lessonTimeService.getAll()).thenReturn(singletonList(retrievedLessonTime));

        mockMvc.perform(get("/lessonstimes"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("lessonstimes/lessonstimes"))
                .andExpect(model().attribute("lessonstimes", is(singletonList(retrievedLessonTime))));
    }

    @Test
    void whenRedirectToSaveForm_thenRedirectedToAddingForm() throws Exception {
        mockMvc.perform(get("/lessonstimes/new"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("lessonstimes/new"));
    }

    @Test
    void whenEdit_thenAddedLessonTimeModelWithGivenIdAndRedirectedToFilledEditingForm() throws Exception {
        when(lessonTimeService.getById(1)).thenReturn(Optional.of(retrievedLessonTime));

        mockMvc.perform(get("/lessonstimes/edit/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("lessonstimes/edit"))
                .andExpect(model().attribute("lessontime", is(retrievedLessonTime)));
    }
}