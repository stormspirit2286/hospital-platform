package com.duy.hospital.authservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.refresh-token")
public record RefreshTokenProperties(
        long ttlDays
) {
}
