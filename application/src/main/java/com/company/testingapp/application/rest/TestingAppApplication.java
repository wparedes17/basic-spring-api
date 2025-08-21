package com.company.testingapp.application.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.company.testingapp"
})

public class TestingAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestingAppApplication.class, args);
    }
}