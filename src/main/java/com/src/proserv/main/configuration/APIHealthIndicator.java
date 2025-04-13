package com.src.proserv.main.configuration;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class APIHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // Custom health check logic
        boolean isHealthy = checkCustomService();
        if (isHealthy) {
            return Health.up().withDetail("Admin API", "Running").build();
        } else {
            return Health.down().withDetail("Admin API", "Down").build();
        }
    }

    private boolean checkCustomService() {
        // Your custom logic to check health (e.g., database status, external API)
        return true;
    }
}