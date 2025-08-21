package com.company.testingapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Basic integration test to verify that the Spring application context loads successfully.
 * This test ensures that all beans can be created and the application starts without errors.
 */
@SpringBootTest
@ActiveProfiles("test")
class TestingAppApplicationTests {

    /**
     * Test that verifies the Spring application context loads without any configuration errors.
     * This is a smoke test that catches basic configuration issues early.
     */
    @Test
    void contextLoads() {
        // This test will pass if the application context loads successfully
        // Spring Boot will automatically fail this test if there are any context loading issues
    }

    /**
     * Test that verifies the main method can be invoked without throwing exceptions.
     * This ensures the application entry point is properly configured.
     */
    @Test
    void mainMethodExecutes() {
        // Test that main method doesn't throw exceptions during startup verification
        // We're not actually starting the server, just testing the method exists and is callable
        TestingAppApplication.main(new String[]{
            "--spring.main.web-environment=false",
            "--spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration"
        });
    }
}
