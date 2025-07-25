package com.learning.tracker.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AuthRequestDTO(
        @NotNull(message = "Email cannot be null")
        @NotBlank(message = "Email cannot be blank")
        String emailAddress,
        @NotNull(message = "Password cannot be null")
        String password

) { }
