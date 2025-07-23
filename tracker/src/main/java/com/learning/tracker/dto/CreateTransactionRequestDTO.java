package com.learning.tracker.dto;

import com.learning.tracker.entity.TransactionEntity;
import com.learning.tracker.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;

@Builder
public record CreateTransactionRequestDTO(
        @NotNull(message = "Transaction Name should not be null")
        @NotBlank(message = "Transaction Name should not be blank")
        String name,
        @NotNull(message = "Amount should not be null")
        @PositiveOrZero(message = "Amount should be positive")
        Double amount,
        @NotNull(message = "Transaction Type should not be null")
        TransactionType type,
        @PastOrPresent(message = "Date should be in the past or present")
        Instant date,
        @NotNull(message = "User ID should not be empty") Long userId
) {
    public CreateTransactionRequestDTO {
        if(name != null){
            name = name.trim();
        }
    }

    public static TransactionEntity toEntity(CreateTransactionRequestDTO transaction) {
        return TransactionEntity.builder()
                .amount(transaction.amount())
                .transactionType(transaction.type())
                .name(transaction.name())
                .userId(transaction.userId())
                .build();
    }
}
