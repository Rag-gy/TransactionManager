package com.learning.tracker.dto.auth;

public record ValidateTokenResponseDTO(
    boolean valid,
    String message,
    Long expiresIn
) {}
