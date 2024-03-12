package com.rbnacharya.trafficinsight.web.controller;

import com.rbnacharya.trafficinsight.dto.TrackRequestDto;
import com.rbnacharya.trafficinsight.errors.validation.CustomerNotFoundValidationError;
import com.rbnacharya.trafficinsight.integration.JsonBuilder;
import com.rbnacharya.trafficinsight.repository.CustomerRepository;
import com.rbnacharya.trafficinsight.repository.IPBlacklistRepository;
import com.rbnacharya.trafficinsight.repository.UABlacklistRepository;
import com.rbnacharya.trafficinsight.service.StatsService;
import com.rbnacharya.trafficinsight.service.TrackRequestValidatorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrackRequestValidatorService requestValidatorService;

    @MockBean
    private StatsService statsService;


    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private IPBlacklistRepository ipBlacklistRepository;

    @MockBean
    private UABlacklistRepository uaBlacklistRepository;


    @Test
    void handleInvalidRequest() {
        Mockito.when(requestValidatorService.validateRequest(Mockito.any(TrackRequestDto.class))).thenThrow(new CustomerNotFoundValidationError());
        String json = JsonBuilder.buildRequestJson(1L, 1L);
        assertDoesNotThrow(() ->
                mockMvc.perform(post("/request").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest())
        );
    }

    @Test
    void handleValidRequest() {
        Mockito.doReturn(true).when(requestValidatorService).validateRequest(Mockito.any(TrackRequestDto.class));
        String json = JsonBuilder.withDefaultTime(1L).build();
        assertDoesNotThrow(() ->
                mockMvc.perform(post("/request").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk())
        );
    }

    @Test
    void handleInvalidRequestStructure() {
        String json = "{\n" +
                "  \"customerID\": 1a\n" +
                "}";
        assertDoesNotThrow(() ->
                mockMvc.perform(post("/request").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest())
        );
    }

    @Test
    void handleMissingRequestStructure() {
        String json = "{\n" +
                "  \"customerID\": 1\n" +
                "}";
        assertDoesNotThrow(() ->
                mockMvc.perform(post("/request").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest())
        );
    }
}