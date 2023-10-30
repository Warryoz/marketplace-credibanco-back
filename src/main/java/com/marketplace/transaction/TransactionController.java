package com.marketplace.transaction;

import com.marketplace.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getTransactions() {
        List<TransactionDto> transactions = transactionService.getTransactions();
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/create")
    public ResponseEntity<TransactionDto> createTransaction(@Valid @RequestBody CreateTransactionDto createTransactionDto) {
        TransactionDto newTransaction = transactionService.createTransaction(createTransactionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTransaction);
    }

    @PostMapping("/cancel/{transactionId}")
    public ResponseEntity<String> cancelTransaction(@PathVariable Long transactionId) {
        boolean isCanceled = transactionService.cancelTransaction(transactionId);

        if (isCanceled) {
            return ResponseEntity.ok("Transaction canceled successfully");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transaction cannot be canceled after 24 hours.");
    }
}

