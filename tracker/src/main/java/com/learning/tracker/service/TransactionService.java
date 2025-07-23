package com.learning.tracker.service;

import com.learning.tracker.dto.CreateTransactionRequestDTO;
import com.learning.tracker.dto.TransactionResponseDTO;
import com.learning.tracker.dto.UpdateTransactionRequestDTO;
import com.learning.tracker.entity.TransactionEntity;
import com.learning.tracker.enums.TransactionCategoryEnum;
import com.learning.tracker.enums.TransactionTypeEnum;
import com.learning.tracker.exception.ResourceNotFoundException;
import com.learning.tracker.exception.TransactionException;
import com.learning.tracker.exception.ValidationException;
import com.learning.tracker.repository.TransactionRepository;
import com.learning.tracker.specification.TransactionSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;


    @Transactional
    public Long addTransaction(CreateTransactionRequestDTO transactionDTO){
        try{
            TransactionEntity transaction = CreateTransactionRequestDTO.toEntity(transactionDTO);
            TransactionEntity savedTransaction = transactionRepository.save(transaction);
            log.info("Transaction added successfully");
            return savedTransaction.getId();
        } catch (Exception e) {
            log.error("Error while adding transaction: {}", e.getMessage(), e);
            throw new TransactionException("Failed to add transaction" + e.getMessage(), e);
        }
    }

    @Transactional
    public List<TransactionResponseDTO> getAllTransactions(
            Long userId, TransactionTypeEnum type, TransactionCategoryEnum category
    ) {
        try {
            log.info("Fetching transactions for userId: {} with type: {}", userId, type);

            Specification<TransactionEntity> spec = TransactionSpecification.withFilters(userId, type, category);
            List<TransactionEntity> transactions = transactionRepository.findAll(spec);

            if (transactions.isEmpty()) {
                log.warn("No transactions found for userId: {} with type: {}", userId, type);
                return List.of();
            }
            List<TransactionResponseDTO> result = transactions.stream()
                    .map(TransactionResponseDTO::fromEntity)
                    .toList();
            log.info("Found {} transactions for userId: {} with type: {}", result.size(), userId, type);
            return result;
        }
        catch (Exception e) {
            log.error("Error while fetching transactions: {}", e.getMessage(), e);
            throw new TransactionException("Failed to fetch transactions: " + e.getMessage(), e);
        }
    }

    @Transactional
    public TransactionResponseDTO getTransactionById(Long id) {
        Optional<TransactionEntity> transactionOpt = transactionRepository.findById(id);
        if (transactionOpt.isPresent()){
            log.info("Transaction found with id: {}", id);
            TransactionEntity transaction = transactionOpt.get();
            return TransactionResponseDTO.fromEntity(transaction);
        } else {
            log.warn("Transaction with id {} not found", id);
            throw new ResourceNotFoundException("Transaction not found with id: " + id);
        }
    }

    @Transactional
    public void updateTransaction(Long transactionId, UpdateTransactionRequestDTO updateRequest) {
        if (!updateRequest.hasUpdates()){
            String message = "No valid fields provided for update for transaction with id: " + transactionId;
            log.warn(message);
            throw new ValidationException(message);
        }
        updateRequest.validateUpdate();
        Optional<TransactionEntity> transactionOpt = transactionRepository.findById(transactionId);
        if (transactionOpt.isEmpty()) {
            log.warn("Transaction with id {} not found for update", transactionId);
            throw new ResourceNotFoundException("Transaction not found with id: " + transactionId);
        }
        try {
            TransactionEntity transaction = transactionOpt.get();
            TransactionEntity updatedTransaction = updateRequest.applyToEntity(transaction);
            transactionRepository.save(updatedTransaction);
            log.info("Transaction updated successfully with id: {}", transactionId);
        } catch (Exception e) {
            log.error("Error while updating transaction with id {}: {}", transactionId, e.getMessage(), e);
            throw new TransactionException("Failed to update transaction with id " + transactionId + ": " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteTransaction(Long id) {
        if(!transactionRepository.existsById(id)) {
            log.warn("Transaction with id {} not found for deletion", id);
            throw new ResourceNotFoundException("Transaction not found for id: " + id);
        }
        try{
            transactionRepository.deleteById(id);
            log.info("Transaction with id {} deleted successfully", id);
        } catch (Exception e) {
            log.error("Error while deleting transaction with id {}: {}", id, e.getMessage(), e);
            throw new TransactionException("Failed to delete transaction with id " + id + ": " + e.getMessage(), e);
        }
    }
}
