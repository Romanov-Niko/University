package com.foxminded.university.controller;

import com.foxminded.university.service.DayScheduleService;
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

import static com.foxminded.university.TestData.*;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@ExtendWith(MockitoExtension.class)
class DayScheduleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DayScheduleService dayScheduleService;

    @InjectMocks
    private DayScheduleController dayScheduleController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(dayScheduleController).build();
    }

    @Test
    void whenSearchScheduleForTeacher_thenRedirectedToSearchPage() throws Exception {
        mockMvc.perform(get("/daysschedules/search/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("daysschedules/search"))
                .andExpect(model().attribute("person", is("teacher")))
                .andExpect(model().attribute("id", is(1)));
    }

    @Test
    void whenSearchScheduleForStudent_thenRedirectedToSearchPage() throws Exception {
        mockMvc.perform(get("/daysschedules/search/student/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("daysschedules/search"))
                .andExpect(model().attribute("person", is("student")))
                .andExpect(model().attribute("id", is(1)));
    }

    @Test
    void whenViewDailyStudentSchedule_thenRedirectedToScheduleViewingPageWithModelThatContainsListOfLessons() throws Exception {
        when(dayScheduleService.getByDateForStudent(1, LocalDate.parse("2017-06-01"))).thenReturn(Optional.of(retrievedDaySchedule));

        mockMvc.perform(post("/daysschedules/student")
                .param("action", "day")
                .param("date", "2017-06-01")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("daysschedules/daysschedules"))
                .andExpect(model().attribute("lessons", is(retrievedDaySchedule.getLessons())));
    }

    @Test
    void whenViewMonthlyStudentSchedule_thenRedirectedToScheduleViewingPageWithModelThatContainsListOfLessons() throws Exception {
        when(dayScheduleService.getByMonthForStudent(1, LocalDate.parse("2017-06-01"))).thenReturn(singletonList(retrievedDaySchedule));

        mockMvc.perform(post("/daysschedules/student")
                .param("action", "month")
                .param("date", "2017-06-01")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("daysschedules/daysschedules"))
                .andExpect(model().attribute("lessons", is(retrievedDaySchedule.getLessons())));
    }

    @Test
    void whenViewDailyTeacherSchedule_thenRedirectedToScheduleViewingPageWithModelThatContainsListOfLessons() throws Exception {
        when(dayScheduleService.getByDateForTeacher(1, LocalDate.parse("2017-06-01"))).thenReturn(Optional.of(retrievedDaySchedule));

        mockMvc.perform(post("/daysschedules/teacher")
                .param("action", "day")
                .param("date", "2017-06-01")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("daysschedules/daysschedules"))
                .andExpect(model().attribute("lessons", is(retrievedDaySchedule.getLessons())));
    }

    @Test
    void whenViewMonthlyTeacherSchedule_thenRedirectedToScheduleViewingPageWithModelThatContainsListOfLessons() throws Exception {
        when(dayScheduleService.getByMonthForTeacher(1, LocalDate.parse("2017-06-01"))).thenReturn(singletonList(retrievedDaySchedule));

        mockMvc.perform(post("/daysschedules/teacher")
                .param("action", "month")
                .param("date", "2017-06-01")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("daysschedules/daysschedules"))
                .andExpect(model().attribute("lessons", is(retrievedDaySchedule.getLessons())));
    }
}