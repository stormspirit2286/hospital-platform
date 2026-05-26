package com.duy.hospital.authservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.jwt")
public record JwtProperties(
        String issuer,
        String secret,
        long accessTokenTtlMinutes
) {
}
