package com.myfinapp.controller;

import com.myfinapp.model.Transaction;
import com.myfinapp.repository.CategoryRepository;
import com.myfinapp.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private final CategoryRepository categoryRepository;

    // Constructor-based dependency injection
    public TransactionController(TransactionService transactionService, CategoryRepository categoryRepository) {
        this.transactionService = transactionService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Long id){
        Transaction transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction).getBody();
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        if (transaction.getCategory().getId() == null) {
            categoryRepository.save(transaction.getCategory());
        }

        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    // Update the whole transaction (Category, Description, Amount, Date and Recurring)
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateFullTransaction(@PathVariable Long id, @RequestBody Transaction transactionDetails) {
        Transaction updatedTransaction = transactionService.updateFullTransaction(id, transactionDetails);
        return ResponseEntity.ok(updatedTransaction);
    }

    // Update the category of the transaction
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateCategoryTransaction(@PathVariable Long id, @RequestBody Transaction transactionDetails) {
        Transaction updatedTransaction = transactionService.updateCategoryTransaction(id, transactionDetails);
        return ResponseEntity.ok(updatedTransaction);
    }

    // Update the description of the transaction
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateDescriptionTransaction(@PathVariable Long id, @RequestBody Transaction transactionDetails) {
        Transaction updatedTransaction = transactionService.updateDescriptionTransaction(id, transactionDetails);
        return ResponseEntity.ok(updatedTransaction);
    }

    // Update the amount of the transaction
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateAmountTransaction(@PathVariable Long id, @RequestBody Transaction transactionDetails) {
        Transaction updatedTransaction = transactionService.updateAmountTransaction(id, transactionDetails);
        return ResponseEntity.ok(updatedTransaction);
    }

    // Update the date of the transaction
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateDateTransaction(@PathVariable Long id, @RequestBody Transaction transactionDetails) {
        Transaction updatedTransaction = transactionService.updateDateTransaction(id, transactionDetails);
        return ResponseEntity.ok(updatedTransaction);
    }

    // Update the recurring state of the transaction
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateRecurringTransaction(@PathVariable Long id, @RequestBody Transaction transactionDetails) {
        Transaction updatedTransaction = transactionService.updateRecurringTransaction(id, transactionDetails);
        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}