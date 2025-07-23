package com.learning.tracker.specification;

import com.learning.tracker.entity.TransactionEntity;
import com.learning.tracker.enums.TransactionType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class TransactionSpecification {

    public static Specification<TransactionEntity> hasUserId(Long userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? null : criteriaBuilder.equal(root.get("userId"), userId);
    }

    public  static Specification<TransactionEntity> hasTransactionType(TransactionType transactionType) {
        return (root, query, criteriaBuilder) ->
                transactionType == null ? null : criteriaBuilder.equal(root.get("transactionType"), transactionType);
    }

    public static Specification<TransactionEntity> withFilters(Long userId, TransactionType transactionType) {
        Specification<TransactionEntity> spec = Specification.allOf();
        if (userId != null) {
            spec = spec.and(hasUserId(userId));
        }
        if (transactionType != null) {
            spec = spec.and(hasTransactionType(transactionType));
        }
        return spec;
    }
}
