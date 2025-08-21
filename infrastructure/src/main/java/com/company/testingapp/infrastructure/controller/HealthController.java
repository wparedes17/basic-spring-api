package com.company.testingapp.infrastructure.controller;

import com.company.testingapp.domain.dto.HealthResponse;
import com.company.testingapp.domain.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for health check endpoints
 * Provides API endpoints to check the application health status
 */
@RestController
@RequestMapping("/v1")
public class HealthController {

    private final HealthService healthService;

    @Autowired
    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    /**
     * Health check endpoint
     * Returns the current health status of the application
     *
     * @return ResponseEntity containing HealthResponse with status information
     */
    @GetMapping("/healthcheck")
    public ResponseEntity<HealthResponse> healthCheck() {
        HealthResponse healthResponse = healthService.getHealthStatus();
        return ResponseEntity.ok(healthResponse);
    }
}
