package com.learning.tracker.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
        String secret,
        long accessTokenValidityInSeconds,
        long refreshTokenValidityInSeconds,
        String issuer
) {}
