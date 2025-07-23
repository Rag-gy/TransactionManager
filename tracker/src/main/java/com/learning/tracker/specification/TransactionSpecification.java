package com.learning.tracker.specification;

import com.learning.tracker.entity.TransactionEntity;
import com.learning.tracker.enums.TransactionCategoryEnum;
import com.learning.tracker.enums.TransactionTypeEnum;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSpecification {

    public static Specification<TransactionEntity> hasUserId(Long userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? null : criteriaBuilder.equal(root.get("userId"), userId);
    }

    public  static Specification<TransactionEntity> hasTransactionType(TransactionTypeEnum transactionTypeEnum) {
        return (root, query, criteriaBuilder) ->
                transactionTypeEnum == null ? null : criteriaBuilder.equal(root.get("transactionType"), transactionTypeEnum);
    }

    public static Specification<TransactionEntity> hasCategory(TransactionCategoryEnum category) {
        return (root, query, criteriaBuilder) ->
                category == null ? null : criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<TransactionEntity> withFilters(
            Long userId,
            TransactionTypeEnum transactionTypeEnum,
            TransactionCategoryEnum category
    ) {
        Specification<TransactionEntity> spec = Specification.allOf();
        if (userId != null) {
            spec = spec.and(hasUserId(userId));
        }
        if (transactionTypeEnum != null) {
            spec = spec.and(hasTransactionType(transactionTypeEnum));
        }
        if(category != null) {
            spec = spec.and(hasCategory(category));
        }
        return spec;
    }
}
