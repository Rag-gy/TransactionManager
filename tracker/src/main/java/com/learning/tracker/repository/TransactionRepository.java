package com.learning.tracker.repository;

import com.learning.tracker.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>,
        JpaSpecificationExecutor<TransactionEntity> {
    List<TransactionEntity> findByName(String name);
    long countByUserId(Long userId);
}
