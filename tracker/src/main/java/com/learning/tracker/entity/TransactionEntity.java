package com.learning.tracker.entity;

import com.learning.tracker.enums.TransactionCategoryEnum;
import com.learning.tracker.enums.TransactionTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
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
    TransactionTypeEnum transactionTypeEnum;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "category", nullable = false)
    private TransactionCategoryEnum category;

}
