package com.company.testingapp.infrastructure.integration;

import com.company.testingapp.domain.dto.HealthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the Health endpoint
 * Tests the complete flow from HTTP request to response
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "management.endpoints.web.exposure.include=health",
    "logging.level.com.example.testingapp=DEBUG"
})
class HealthIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void healthEndpoint_ShouldReturnHealthyStatus_WhenApplicationIsRunning() {
        // Given
        String url = "http://localhost:" + port + "/api/v1/health";

        // When
        ResponseEntity<HealthResponse> response = restTemplate.getForEntity(url, HealthResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo("UP");
        assertThat(response.getBody().getDetails()).isEqualTo("All systems operational");
        assertThat(response.getBody().getTimestamp()).isNotNull();
        assertThat(response.getBody().getVersion()).isNotNull();
    }

    @Test
    void healthEndpoint_ShouldReturnCorrectHeaders_WhenCalled() {
        // Given
        String url = "http://localhost:" + port + "/api/v1/health";

        // When
        ResponseEntity<HealthResponse> response = restTemplate.getForEntity(url, HealthResponse.class);

        // Then
        assertThat(response.getHeaders().getContentType().toString())
            .contains("application/json");
        assertThat(response.getHeaders().get("Cache-Control"))
            .isNotNull();
    }

    @Test
    void healthEndpoint_ShouldReturnValidJson_WhenCalled() throws Exception {
        // Given
        String url = "http://localhost:" + port + "/api/v1/healthcheck";

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // Verify JSON structure
        HealthResponse healthResponse = objectMapper.readValue(response.getBody(), HealthResponse.class);
        assertThat(healthResponse.getStatus()).isEqualTo("UP");
        assertThat(healthResponse.getDetails()).isEqualTo("All systems operational");
        assertThat(healthResponse.getTimestamp()).isNotNull();
        assertThat(healthResponse.getVersion()).isNotNull();
    }

    @Test
    void healthEndpoint_ShouldBeAccessible_WithoutAuthentication() {
        // Given
        String url = "http://localhost:" + port + "/api/v1/healthcheck";

        // When
        ResponseEntity<HealthResponse> response = restTemplate.getForEntity(url, HealthResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // No 401 Unauthorized or 403 Forbidden should be returned
        assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void healthEndpoint_ShouldReturnConsistentResponse_WhenCalledMultipleTimes() {
        // Given
        String url = "http://localhost:" + port + "/api/v1/healthcheck";

        // When - Call multiple times
        ResponseEntity<HealthResponse> response1 = restTemplate.getForEntity(url, HealthResponse.class);
        ResponseEntity<HealthResponse> response2 = restTemplate.getForEntity(url, HealthResponse.class);
        ResponseEntity<HealthResponse> response3 = restTemplate.getForEntity(url, HealthResponse.class);

        // Then - All responses should be successful with same status
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response1.getBody().getStatus()).isEqualTo("UP");
        assertThat(response2.getBody().getStatus()).isEqualTo("UP");
        assertThat(response3.getBody().getStatus()).isEqualTo("UP");

        assertThat(response1.getBody().getDetails()).isEqualTo("All systems operational");
        assertThat(response2.getBody().getDetails()).isEqualTo("All systems operational");
        assertThat(response3.getBody().getDetails()).isEqualTo("All systems operational");
    }

    @Test
    void healthEndpoint_ShouldHaveReasonableResponseTime_WhenCalled() {
        // Given
        String url = "http://localhost:" + port + "/api/v1/healthcheck";
        
        // When
        long startTime = System.currentTimeMillis();
        ResponseEntity<HealthResponse> response = restTemplate.getForEntity(url, HealthResponse.class);
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Health check should respond within 1 second
        assertThat(responseTime).isLessThan(1000);
    }

    @Test
    void nonExistentEndpoint_ShouldReturn404_WhenCalled() {
        // Given
        String url = "http://localhost:" + port + "/api/v1/nonexistent";

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
