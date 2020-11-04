package com.foxminded.university.controller;

import com.foxminded.university.service.LessonTimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.foxminded.university.TestData.retrievedLessonTime;
import static com.foxminded.university.TestData.updatedLessonTime;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        when(lessonTimeService.findAll()).thenReturn(singletonList(retrievedLessonTime));

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
        when(lessonTimeService.findById(1)).thenReturn(Optional.of(retrievedLessonTime));

        mockMvc.perform(get("/lessonstimes/edit/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("lessonstimes/edit"))
                .andExpect(model().attribute("lessontime", is(retrievedLessonTime)));
    }

    @Test
    void whenDelete_thenCalledLessonTimeServiceDeleteWithGivenIdAndRedirectedToPageWithListOfLessonsTimes() throws Exception {
        mockMvc.perform(get("/lessonstimes/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lessonstimes"));

        verify(lessonTimeService, times(1)).delete(1);
    }

    @Test
    void whenSave_thenCalledLessonTimeServiceSaveWithGivenLessonTimeAndRedirectedToPageWithListOfLessonsTimes() throws Exception {
        mockMvc.perform(post("/lessonstimes/save")
                .flashAttr("lessontime", retrievedLessonTime))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lessonstimes"));

        verify(lessonTimeService, times(1)).save(retrievedLessonTime);
    }

    @Test
    void whenUpdate_thenCalledLessonTimeServiceSaveWithGivenLessonTimeAndRedirectedToPageWithListOfLessonsTimes() throws Exception {
        mockMvc.perform(post("/lessonstimes/update/1")
                .flashAttr("lessontime", updatedLessonTime))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lessonstimes"));

        verify(lessonTimeService, times(1)).update(updatedLessonTime);
    }
}