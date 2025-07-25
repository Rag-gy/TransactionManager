package com.learning.tracker.dto.users;

import com.learning.tracker.enums.UserRoleEnum;

import java.time.LocalDateTime;

public record UserDTO(
        Long id,
        String emailAddress,
        String firstName,
        String lastName,
        Boolean archived,
        UserRoleEnum role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
