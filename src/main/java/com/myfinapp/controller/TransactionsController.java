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
    public List<Transactions> getAllFins() {
        return transactionsService.getAllFins();
    }

    @PostMapping
    public ResponseEntity<Transactions> createFin(@RequestBody Transactions transactions) {
        Transactions createdTransactions = transactionsService.createFin(transactions);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransactions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transactions> updateFin(@PathVariable Long id, @RequestBody Transactions transactionsDetails) {
        Transactions updatedTransactions = transactionsService.updateFin(id, transactionsDetails);
        return ResponseEntity.ok(updatedTransactions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFin(@PathVariable Long id) {
        transactionsService.deleteFin(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content when successful
    }
}