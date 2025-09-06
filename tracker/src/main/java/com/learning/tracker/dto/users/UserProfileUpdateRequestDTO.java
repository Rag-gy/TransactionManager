package com.learning.tracker.dto.users;

import com.learning.tracker.enums.UserRoleEnum;
import jakarta.validation.constraints.Size;

public record UserProfileUpdateRequestDTO(
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters long")
    String firstName,
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters long")
    String lastName,
    UserRoleEnum role,
    boolean archived
) {
}
