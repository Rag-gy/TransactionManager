package com.learning.tracker.dto;

import com.learning.tracker.enums.UserRoleEnum;

import java.time.Instant;

public record UsersDTO(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        Boolean archived,
        UserRoleEnum role,
        Instant createdAt
) {
}
