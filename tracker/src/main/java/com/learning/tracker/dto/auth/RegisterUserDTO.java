package com.learning.tracker.dto.auth;

import com.learning.tracker.enums.UserRoleEnum;
import jakarta.validation.constraints.*;

public record RegisterUserDTO(
        @NotNull(message = "Email address is required")
        @Email(message = "Email address must be valid")
        String emailAddress,
        @NotNull(message = "First name is required")
        @NotBlank(message = "First name should not be blank")
        @Size(min=2, max = 50, message = "First name should be between 2 and 50 characters")
        String firstName,
        @NotNull(message = "Last name is required")
        @NotBlank(message = "Last name should not be blank")
        @Size(min=2, max = 50, message = "Last name should be between 2 and 50 characters")
        String lastName,
        @NotNull(message = "User role is required")
        UserRoleEnum role,
        @NotNull(message = "Password is required")
        @Size(message = "Password must be at least 8 characters long", min = 8)
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@$!%*?&])[A-Za-z\\\\d@$!%*?&]",
                message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
        )
        String password
) {
}


