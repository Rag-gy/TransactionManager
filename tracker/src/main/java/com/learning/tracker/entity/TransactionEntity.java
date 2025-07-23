package com.learning.tracker.entity;

import com.learning.tracker.enums.TransactionCategory;
import com.learning.tracker.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long Id;

    @Column(name="user_id", nullable = false)
    Long userId;

    @Column(name="name", nullable = false)
    String name;

    @Column(name="amount", nullable = false)
    Double amount;

    @Column(name="type", nullable = false)
    TransactionType transactionType;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "category", nullable = false)
    private TransactionCategory category;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
