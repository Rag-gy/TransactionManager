package com.learning.tracker.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TokenRefreshRequestDTO(
   @NotBlank(message = "Current token is required")
   @NotNull
   String authToken
) {}
