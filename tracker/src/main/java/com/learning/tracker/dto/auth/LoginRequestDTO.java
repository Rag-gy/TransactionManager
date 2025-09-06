package com.learning.tracker.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "The email address is required for login") String emailAddress,
        @NotBlank(message = "The password is required for login") String password
) {}
