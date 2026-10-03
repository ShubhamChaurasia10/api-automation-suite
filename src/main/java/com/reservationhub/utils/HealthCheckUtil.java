package com.reservationhub.utils;

import io.restassured.RestAssured;

public class HealthCheckUtil {

    public static void waitForApiToWakeUp() {
        int maxAttempts = 10;
        int delayMs = 3000;

        for (int i = 0; i < maxAttempts; i++) {
            try {
                int status = RestAssured.get("/ping").getStatusCode();
                if (status == 201 || status == 200) {
                    return;
                }
            } catch (Exception ignored) {}

            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        throw new RuntimeException("API warm-up failed: /ping endpoint did not respond within timeout.");
    }
}