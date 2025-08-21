package com.company.testingapp.infrastructure.services;

import com.company.testingapp.domain.dto.HealthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HealthServiceImplTest {

    @InjectMocks
    private HealthServiceImpl healthService;

    @BeforeEach
    void setUp() {
        // Setup can be added here if needed for future enhancements
    }

    @Test
    void getHealthStatus_ShouldReturnHealthyStatus() {
        // When
        HealthResponse response = healthService.getHealthStatus();

        // Then
        assertNotNull(response);
        assertEquals("UP", response.getStatus());
        assertEquals("Testing App", response.getService());
        assertEquals("1.0.0", response.getVersion());
        assertEquals("development", response.getEnvironment());
        assertNotNull(response.getTimestamp());
        assertTrue(response.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(response.getTimestamp().isAfter(LocalDateTime.now().minusSeconds(5)));
    }

    @Test
    void getHealthStatus_ShouldReturnCurrentTimestamp() {
        // Given
        LocalDateTime beforeCall = LocalDateTime.now();

        // When
        HealthResponse response = healthService.getHealthStatus();

        // Then
        LocalDateTime afterCall = LocalDateTime.now();

        assertNotNull(response.getTimestamp());
        assertTrue(response.getTimestamp().isAfter(beforeCall.minusSeconds(1)) ||
                response.getTimestamp().isEqual(beforeCall.minusSeconds(1)));
        assertTrue(response.getTimestamp().isBefore(afterCall.plusSeconds(1)) ||
                response.getTimestamp().isEqual(afterCall.plusSeconds(1)));
    }

    @Test
    void getHealthStatus_ShouldReturnConsistentResponse() {
        // When
        HealthResponse response1 = healthService.getHealthStatus();
        HealthResponse response2 = healthService.getHealthStatus();

        // Then
        assertEquals(response1.getStatus(), response2.getStatus());
        assertEquals(response1.getService(), response2.getService());
        assertEquals(response1.getVersion(), response2.getVersion());
        assertEquals(response1.getEnvironment(), response2.getEnvironment());
        // Timestamps should be different (assuming some time passes between calls)
        assertNotNull(response1.getTimestamp());
        assertNotNull(response2.getTimestamp());
    }

    @Test
    void getHealthStatus_StatusShouldBeUp() {
        // When
        HealthResponse response = healthService.getHealthStatus();

        // Then
        assertEquals("UP", response.getStatus());
    }

    @Test
    void getHealthStatus_AppNameShouldBeTestingApp() {
        // When
        HealthResponse response = healthService.getHealthStatus();

        // Then
        assertEquals("Testing App", response.getService());
    }

    @Test
    void getHealthStatus_VersionShouldBeCorrect() {
        // When
        HealthResponse response = healthService.getHealthStatus();

        // Then
        assertEquals("1.0.0", response.getVersion());
    }

    @Test
    void getHealthStatus_EnvironmentShouldBeDevelopment() {
        // When
        HealthResponse response = healthService.getHealthStatus();

        // Then
        assertEquals("development", response.getEnvironment());
    }

    @Test
    void getHealthStatus_ShouldNotReturnNull() {
        // When
        HealthResponse response = healthService.getHealthStatus();

        // Then
        assertNotNull(response);
        assertNotNull(response.getStatus());
        assertNotNull(response.getService());
        assertNotNull(response.getVersion());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void getHealthStatus_MultipleCalls_ShouldReturnValidResponses() {
        // When & Then
        for (int i = 0; i < 10; i++) {
            HealthResponse response = healthService.getHealthStatus();

            assertNotNull(response);
            assertEquals("UP", response.getStatus());
            assertEquals("Testing App", response.getService());
            assertEquals("1.0.0", response.getVersion());
            assertEquals("development", response.getEnvironment());
            assertNotNull(response.getTimestamp());
        }
    }

    @Test
    void getDetailedHealthStatus_ShouldReturnDetailedHealthyStatus() {
        // When
        HealthResponse response = healthService.getDetailedHealthStatus();

        // Then
        assertNotNull(response);
        assertEquals("UP", response.getStatus());
        assertEquals("Testing App", response.getService());
        assertEquals("1.0.0", response.getVersion());
        assertEquals("development", response.getEnvironment());
        assertEquals("Available since startup", response.getUptime());
        assertEquals("All systems operational", response.getDetails());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void getDetailedHealthStatus_ShouldReturnCurrentTimestamp() {
        // Given
        LocalDateTime beforeCall = LocalDateTime.now();

        // When
        HealthResponse response = healthService.getDetailedHealthStatus();

        // Then
        LocalDateTime afterCall = LocalDateTime.now();

        assertNotNull(response.getTimestamp());
        assertTrue(response.getTimestamp().isAfter(beforeCall.minusSeconds(1)) ||
                response.getTimestamp().isEqual(beforeCall.minusSeconds(1)));
        assertTrue(response.getTimestamp().isBefore(afterCall.plusSeconds(1)) ||
                response.getTimestamp().isEqual(afterCall.plusSeconds(1)));
    }

    @Test
    void getDetailedHealthStatus_MultipleCalls_ShouldReturnValidResponses() {
        // When & Then
        for (int i = 0; i < 5; i++) {
            HealthResponse response = healthService.getDetailedHealthStatus();

            assertNotNull(response);
            assertEquals("UP", response.getStatus());
            assertEquals("Testing App", response.getService());
            assertEquals("1.0.0", response.getVersion());
            assertEquals("development", response.getEnvironment());
            assertEquals("Available since startup", response.getUptime());
            assertEquals("All systems operational", response.getDetails());
            assertNotNull(response.getTimestamp());
        }
    }

    @Test
    void isApplicationReady_ShouldReturnTrue() {
        // When
        boolean isReady = healthService.isApplicationReady();

        // Then
        assertTrue(isReady);
    }

    @Test
    void isApplicationAlive_ShouldReturnTrue() {
        // When
        boolean isAlive = healthService.isApplicationAlive();

        // Then
        assertTrue(isAlive);
    }

    @Test
    void isApplicationReady_MultipleCalls_ShouldReturnConsistentResult() {
        // When & Then
        for (int i = 0; i < 5; i++) {
            boolean isReady = healthService.isApplicationReady();
            assertTrue(isReady);
        }
    }

    @Test
    void isApplicationAlive_MultipleCalls_ShouldReturnConsistentResult() {
        // When & Then
        for (int i = 0; i < 5; i++) {
            boolean isAlive = healthService.isApplicationAlive();
            assertTrue(isAlive);
        }
    }
}