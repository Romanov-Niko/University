package com.foxminded.university.controller;

import com.foxminded.university.config.ApplicationTestConfig;
import com.foxminded.university.service.AudienceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.foxminded.university.TestData.*;
import java.util.Collections;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AudienceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AudienceService audienceService;

    @InjectMocks
    private AudienceController audienceController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(audienceController).build();
    }

    @Test
    void givenAudiencesUrl_whenShowAll_thenReturnedAudiencesHtmlAndModelWithAllAudiences() throws Exception {
        when(audienceService.getAll()).thenReturn(singletonList(retrievedAudience));

        mockMvc.perform(get("/audiences"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("audiences/audiences"))
                .andExpect(model().attribute("audiences", hasSize(1)))
                .andExpect(model().attribute("audiences", hasItem(
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("roomNumber", is(101)),
                                hasProperty("capacity", is(100))
                        )
                )));

        verify(audienceService, times(1)).getAll();
    }

    @Test
    void givenAudiencesNewUrl_whenRedirectToSaveForm_thenReturnedNewHtmlAndEmptyAudienceModel() throws Exception {
        mockMvc.perform(get("/audiences/new"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("audiences/new"))
                .andExpect(model().attribute("audience", hasProperty("id", is(0))))
                .andExpect(model().attribute("audience", hasProperty("roomNumber", is(0))))
                .andExpect(model().attribute("audience", hasProperty("capacity", is(0))));
    }

    @Test
    void givenAudiencesEditUrl_whenEdit_thenReturnedEditHtmlAndAudienceModelWithGivenId() throws Exception {
        when(audienceService.getById(anyInt())).thenReturn(Optional.of(retrievedAudience));

        mockMvc.perform(get("/audiences/edit/1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("audiences/edit"))
                .andExpect(model().attribute("audience", hasProperty("id", is(1))))
                .andExpect(model().attribute("audience", hasProperty("roomNumber", is(101))))
                .andExpect(model().attribute("audience", hasProperty("capacity", is(100))));

        verify(audienceService, times(1)).getById(anyInt());
    }
}