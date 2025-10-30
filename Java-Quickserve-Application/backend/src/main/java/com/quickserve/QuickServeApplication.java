package com.quickserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuickServeApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuickServeApplication.class, args);
        System.out.println("🚀 QuickServe Backend is running on http://localhost:8080");
    }
}