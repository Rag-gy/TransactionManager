package com.learning.tracker.dto.users;

import com.learning.tracker.entity.UserEntity;
import com.learning.tracker.enums.UserRoleEnum;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
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

    public static UserDTO fromEntity(UserEntity user) {
        return UserDTO.builder().
                id(user.id)
                .emailAddress(user.emailAddress)
                .firstName(user.firstName)
                .lastName(user.lastName)
                .archived(user.archived)
                .role(user.role)
                .createdAt(user.createdAt)
                .updatedAt(user.updatedAt)
                .build();
    }
}
