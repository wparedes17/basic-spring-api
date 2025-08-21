package com.company.testingapp.domain.service;

import com.company.testingapp.domain.dto.HealthResponse;

/**
 * Service interface responsible for defining health check business logic contracts.
 * This interface provides methods to check the application's health status
 * and return appropriate health information.
 */
public interface HealthService {

    /**
     * Performs a health check and returns the current health status of the application.
     *
     * @return HealthResponse containing status, timestamp, and application metadata
     */
    HealthResponse getHealthStatus();

    /**
     * Performs a detailed health check that could include additional system checks.
     * Currently returns the same as basic health check, but can be extended
     * to include database connectivity, external service checks, etc.
     *
     * @return HealthResponse with detailed health information
     */
    HealthResponse getDetailedHealthStatus();

    /**
     * Checks if the application is ready to serve traffic.
     * This could include checks for:
     * - Database migrations completed
     * - Cache warming completed
     * - External dependencies available
     *
     * @return true if application is ready, false otherwise
     */
    boolean isApplicationReady();

    /**
     * Checks if the application is alive (basic liveness check).
     * This is typically used by orchestrators like Kubernetes
     * to determine if the application should be restarted.
     *
     * @return true if application is alive, false otherwise
     */
    boolean isApplicationAlive();
}
