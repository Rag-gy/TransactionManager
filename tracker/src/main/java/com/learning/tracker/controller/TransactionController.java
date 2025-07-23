package com.learning.tracker.controller;

import com.learning.tracker.dto.ApiResponseDTO;
import com.learning.tracker.dto.CreateTransactionRequestDTO;
import com.learning.tracker.dto.TransactionResponseDTO;
import com.learning.tracker.dto.UpdateTransactionRequestDTO;
import com.learning.tracker.enums.TransactionCategoryEnum;
import com.learning.tracker.enums.TransactionTypeEnum;
import com.learning.tracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/")
    public ResponseEntity<ApiResponseDTO<Long>> createTransaction(@Valid @RequestBody CreateTransactionRequestDTO transaction) {
        Long transactionId = transactionService.addTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.<Long>builder()
                        .message("Transaction created successfully")
                        .data(transactionId)
                        .build()
                );
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponseDTO<List<TransactionResponseDTO>>> getAllTransactions(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) TransactionTypeEnum type,
            @RequestParam(required = false) TransactionCategoryEnum category
    ) {
        List<TransactionResponseDTO> transactions = transactionService.getAllTransactions(userId, type, category);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponseDTO.<List<TransactionResponseDTO>>builder()
                        .message("Transactions fetched successfully")
                        .data(transactions)
                        .build()
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TransactionResponseDTO>> getTransactionById(
            @PathVariable(required = true) Long id
    ) {
        TransactionResponseDTO transaction = transactionService.getTransactionById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponseDTO.<TransactionResponseDTO>builder()
                        .message("Transaction fetched successfully")
                        .data(transaction)
                        .build()
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Long>> updateTransaction(
            @PathVariable(required = true) Long id,
            @Valid @RequestBody UpdateTransactionRequestDTO transactionRequestDTO
    ) {
        transactionService.updateTransaction(id, transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponseDTO.<Long>builder()
                        .message("Transaction updated successfully")
                        .data(id)
                        .build()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Long>> deleteTransaction(
            @PathVariable(required = true) Long id
    ) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponseDTO.<Long>builder()
                        .message("Transaction deleted successfully")
                        .data(id)
                        .build()
                );
    }
}
