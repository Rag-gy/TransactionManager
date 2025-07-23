package com.learning.tracker.dto;

import com.learning.tracker.entity.TransactionEntity;
import com.learning.tracker.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;

@Builder
public record TransactionResponseDTO(
        @NotNull Long id,
        @NotNull String name,
        @NotNull Double amount,
        @NotNull TransactionType type,
        @NotNull Instant date,
        @NotNull Instant updatedAt,
        @NotNull Instant createdAt,
        @NotNull Long userId
) {

    public static TransactionResponseDTO fromEntity(TransactionEntity entity) {
        return TransactionResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .amount(entity.getAmount())
                .type(entity.getTransactionType())
                .date(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdAt(entity.getCreatedAt())
                .userId(entity.getUserId())
                .build();
    }
}
