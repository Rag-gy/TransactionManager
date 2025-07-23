package com.learning.tracker.dto;

import com.learning.tracker.entity.TransactionEntity;
import com.learning.tracker.enums.TransactionCategoryEnum;
import com.learning.tracker.enums.TransactionTypeEnum;
import com.learning.tracker.exception.ValidationException;

import java.time.Instant;

public record UpdateTransactionRequestDTO (
        String name,
        Double amount,
        TransactionTypeEnum type,
        Instant date,
        TransactionCategoryEnum category
){

    public TransactionEntity applyToEntity(TransactionEntity transaction) {
        if (this.name != null) {
            transaction.setName(this.name.trim());
        }
        if (this.amount != null) {
            transaction.setAmount(this.amount);
        }
        if (this.type != null) {
            transaction.setTransactionTypeEnum(this.type);
        }
        if (this.date != null) {
            transaction.setCreatedAt(this.date);
        }
        if (this.category != null) {
            transaction.setCategory(this.category);
        }
        return transaction;
    }

    public void validateUpdate() {
        if (this.name != null && this.name.isBlank()) {
            throw new ValidationException("Transaction Name should not be blank");
        }
        if (this.amount != null && this.amount < 0) {
            throw new ValidationException("Amount should be positive or zero");
        }
        if (this.date != null && this.date.isAfter(Instant.now())) {
            throw new ValidationException("Date should be in the past or present");
        }
    }

    public boolean hasUpdates() {
        return this.name != null ||
               this.amount != null ||
               this.type != null ||
               this.date != null;
    }
}
