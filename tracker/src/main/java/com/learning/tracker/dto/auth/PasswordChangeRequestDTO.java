package com.learning.tracker.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordChangeRequestDTO(
        @NotBlank(message = "New password cannot be blank")
        @NotNull(message = "New password cannot be null")
        @Size(min=8, message = "New password must be at least 8 characters long")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]",
                message = "New password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
        )
        String newPassword,

        @NotBlank(message = "User email cannot be blank")
        @NotNull(message = "User email cannot be null")
        String emailAddress
) {}
