package com.learning.tracker.dto.auth;

import lombok.Builder;

@Builder
public record AuthResponseDTO(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn,
        Long refreshExpiresIn
) { }
