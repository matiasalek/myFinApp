package com.myfinapp.controller;

import com.myfinapp.model.RecurringTransaction;
import com.myfinapp.service.RecurringTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recurringtransaction")
public class RecurringTransactionController {
    private final RecurringTransactionService recurringTransactionService;

    public RecurringTransactionController(RecurringTransactionService recurringTransactionService){
        this.recurringTransactionService = recurringTransactionService;
    }

    @GetMapping
    public List<RecurringTransaction> getAllRecurringTransaction(){
        return recurringTransactionService.getAllRecurringTransaction();
    }

    @GetMapping("/{id}")
    public RecurringTransaction getRecurringTransactionById(@PathVariable Long id){
        RecurringTransaction recurringTransaction = recurringTransactionService.getRecurringTransactionById(id);
        return ResponseEntity.ok(recurringTransaction).getBody();
    }

    @PostMapping
    public ResponseEntity<RecurringTransaction> createRecurringTransaction(@RequestBody RecurringTransaction recurringTransaction) {
        RecurringTransaction createdRecurringTransaction = recurringTransactionService.createRecurringTransaction(recurringTransaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecurringTransaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecurringTransaction> updateRecurringTransaction(@PathVariable Long id, @RequestBody RecurringTransaction recurringTransactionDetails) {
        RecurringTransaction updatedRecurringTransaction = recurringTransactionService.updateRecurringTransaction(id, recurringTransactionDetails);
        return ResponseEntity.ok(updatedRecurringTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecurringTransaction(@PathVariable Long id) {
        recurringTransactionService.deleteRecurringTransaction(id);
        return ResponseEntity.noContent().build();
    }
}