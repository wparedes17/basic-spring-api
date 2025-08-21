package com.company.testingapp.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for health check responses.
 * Contains essential information about the application's health status.
 */
public class HealthResponse {
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    @JsonProperty("service")
    private String service;
    
    @JsonProperty("version")
    private String version;
    
    @JsonProperty("environment")
    private String environment;
    
    @JsonProperty("uptime")
    private String uptime;
    
    @JsonProperty("details")
    private String details;

    // Default constructor
    public HealthResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with essential fields
    public HealthResponse(String status, String service, String version) {
        this();
        this.status = status;
        this.service = service;
        this.version = version;
    }

    // Full constructor
    public HealthResponse(String status, String service, String version, 
                         String environment, String uptime, String details) {
        this();
        this.status = status;
        this.service = service;
        this.version = version;
        this.environment = environment;
        this.uptime = uptime;
        this.details = details;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "HealthResponse{" +
                "status='" + status + '\'' +
                ", timestamp=" + timestamp +
                ", service='" + service + '\'' +
                ", version='" + version + '\'' +
                ", environment='" + environment + '\'' +
                ", uptime='" + uptime + '\'' +
                ", details=" + details +
                '}';
    }
}
