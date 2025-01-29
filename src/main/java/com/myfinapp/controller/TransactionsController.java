package com.myfinapp.controller;

import com.myfinapp.model.Transactions;
import com.myfinapp.service.TransactionsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/myfin")
public class TransactionsController {

    private final TransactionsService transactionsService;

    // Constructor-based dependency injection
    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @GetMapping
    public List<Transactions> getAllTransactions() {
        return transactionsService.getAllTransactions();
    }

    @PostMapping
    public ResponseEntity<Transactions> createTransaction(@RequestBody Transactions transactions) {
        Transactions createdTransactions = transactionsService.createTransaction(transactions);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransactions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transactions> updateTransaction(@PathVariable Long id, @RequestBody Transactions transactionsDetails) {
        Transactions updatedTransactions = transactionsService.updateTransaction(id, transactionsDetails);
        return ResponseEntity.ok(updatedTransactions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionsService.deleteTransaction(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content when successful
    }
}