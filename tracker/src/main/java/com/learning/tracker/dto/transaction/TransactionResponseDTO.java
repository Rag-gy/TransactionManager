package com.learning.tracker.dto.transaction;

import com.learning.tracker.entity.TransactionEntity;
import com.learning.tracker.enums.TransactionCategoryEnum;
import com.learning.tracker.enums.TransactionTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransactionResponseDTO(
        @NotNull Long id,
        @NotNull String name,
        @NotNull Double amount,
        @NotNull TransactionTypeEnum type,
        @NotNull LocalDateTime date,
        @NotNull LocalDateTime updatedAt,
        @NotNull LocalDateTime createdAt,
        @NotNull Long userId,
        @NotNull TransactionCategoryEnum category
) {

    public static TransactionResponseDTO fromEntity(TransactionEntity entity) {
        return TransactionResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .amount(entity.getAmount())
                .type(entity.getTransactionTypeEnum())
                .date(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdAt(entity.getCreatedAt())
                .userId(entity.getUserId())
                .category(entity.getCategory())
                .build();
    }
}
