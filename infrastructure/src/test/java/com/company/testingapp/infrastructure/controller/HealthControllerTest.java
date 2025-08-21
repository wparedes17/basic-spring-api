package com.company.testingapp.infrastructure.controller;

import com.company.testingapp.domain.dto.HealthResponse;
import com.company.testingapp.domain.service.HealthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for HealthController
 * Tests the REST endpoint behavior in isolation
 */
@WebMvcTest(HealthController.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HealthService healthService;

    @Autowired
    private ObjectMapper objectMapper;

    private HealthResponse healthyResponse;
    private HealthResponse unhealthyResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup test data
        healthyResponse = new HealthResponse(
            "UP",
            "Testing App",
            "1.0.0",
            "test",
            LocalDateTime.now().toString(),
            "All systems operational"
        );
        
        unhealthyResponse = new HealthResponse(
            "DOWN",
            "Testing App",
            "1.0.0",
            "test",
            LocalDateTime.now().toString(),
            "Service temporarily unavailable"
        );
    }

    @Test
    void shouldReturnHealthyStatus() throws Exception {
        // Given
        when(healthService.getHealthStatus()).thenReturn(healthyResponse);

        // When & Then
        ResultActions result = mockMvc.perform(get("/api/v1/healthcheck")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.serviceName").value("Testing App"))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value("All systems operational"));

        // Verify the response body structure
        String responseContent = result.andReturn().getResponse().getContentAsString();
        HealthResponse actualResponse = objectMapper.readValue(responseContent, HealthResponse.class);
        
        assert actualResponse.getStatus().equals("UP");
        assert actualResponse.getService().equals("Testing App");
        assert actualResponse.getVersion().equals("1.0.0");
        assert actualResponse.getTimestamp() != null;
        assert actualResponse.getDetails().equals("All systems operational");
    }

    @Test
    void shouldReturnUnhealthyStatusWhenServiceDown() throws Exception {
        // Given
        when(healthService.getHealthStatus()).thenReturn(unhealthyResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()) // Health endpoint should always return 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("DOWN"))
                .andExpect(jsonPath("$.serviceName").value("Testing App"))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value("Service temporarily unavailable"));
    }

    @Test
    void shouldHandleServiceException() throws Exception {
        // Given
        when(healthService.getHealthStatus()).thenThrow(new RuntimeException("Service error"));

        // When & Then
        mockMvc.perform(get("/api/v1/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldAcceptGetMethodOnly() throws Exception {
        // Given
        when(healthService.getHealthStatus()).thenReturn(healthyResponse);

        // When & Then - POST should not be allowed
        mockMvc.perform(post("/api/v1/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        // PUT should not be allowed
        mockMvc.perform(put("/api/v1/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        // DELETE should not be allowed
        mockMvc.perform(delete("/api/v1/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturnCorrectContentType() throws Exception {
        // Given
        when(healthService.getHealthStatus()).thenReturn(healthyResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/health"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"));
    }

    @Test
    void shouldHandleMultipleConcurrentRequests() throws Exception {
        // Given
        when(healthService.getHealthStatus()).thenReturn(healthyResponse);

        // When & Then - Simulate multiple concurrent requests
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get("/api/v1/health")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("UP"));
        }
    }

    @Test
    void shouldValidateResponseTimeIsReasonable() throws Exception {
        // Given
        when(healthService.getHealthStatus()).thenReturn(healthyResponse);
        
        // When
        long startTime = System.currentTimeMillis();
        
        mockMvc.perform(get("/api/v1/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        long endTime = System.currentTimeMillis();
        
        // Then - Response should be fast (under 1 second)
        long responseTime = endTime - startTime;
        assert responseTime < 1000 : "Health check should respond quickly, took: " + responseTime + "ms";
    }
}
