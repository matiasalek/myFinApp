package com.myfinapp.service;

import com.myfinapp.exception.ResourceNotFoundException;
import com.myfinapp.model.Transaction;
import com.myfinapp.model.RecurringTransaction;
import com.myfinapp.repository.TransactionRepository;
import com.myfinapp.repository.RecurringTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final RecurringTransactionRepository recurringTransactionRepository;

    public TransactionService(TransactionRepository transactionRepository, RecurringTransactionRepository recurringTransactionRepository) {
        this.transactionRepository = transactionRepository;
        this.recurringTransactionRepository = recurringTransactionRepository;
    }

    @Transactional(readOnly = true)
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    public Transaction createTransaction(Transaction transaction) {
        if (transaction.getId() != null) {
            throw new ResourceNotFoundException("Transaction already exists");
        }

        Transaction savedTransaction = transactionRepository.save(transaction);

        if (transaction.isRecurring()) {
            createRecurringTransaction(savedTransaction);
        }

        return savedTransaction;
    }

    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        return transactionRepository.findById(id).map(transaction -> {
            boolean wasRecurring = transaction.isRecurring();
            boolean nowRecurring = transactionDetails.isRecurring();

            transaction.setCategory(transactionDetails.getCategory());
            transaction.setDescription(transactionDetails.getDescription());
            transaction.setAmount(transactionDetails.getAmount());
            transaction.setDate(transactionDetails.getDate());
            transaction.setRecurring(nowRecurring);

            Transaction updatedTransaction = transactionRepository.save(transaction);

            handleRecurringTransactionUpdate(updatedTransaction, wasRecurring, nowRecurring);

            return updatedTransaction;
        }).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    public Transaction patchTransaction(Long id, Map<String, Object> updates) {
        return transactionRepository.findById(id).map(transaction -> {
            boolean wasRecurring = transaction.isRecurring();
            boolean nowRecurring = wasRecurring;

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                switch (entry.getKey()) {
                    case "description":
                        transaction.setDescription((String) entry.getValue());
                        break;
                    case "amount":
                        transaction.setAmount((BigDecimal) entry.getValue());
                        break;
                    case "date":
                        transaction.setDate((LocalDateTime) entry.getValue());
                        break;
                    case "recurring":
                        nowRecurring = (Boolean) entry.getValue();
                        transaction.setRecurring(nowRecurring);
                        break;
                }
            }

            Transaction updatedTransaction = transactionRepository.save(transaction);
            handleRecurringTransactionUpdate(updatedTransaction, wasRecurring, nowRecurring);

            return updatedTransaction;
        }).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found");
        }
        recurringTransactionRepository.deleteById(id);
        transactionRepository.deleteById(id);
    }

    private void createRecurringTransaction(Transaction transaction) {
        RecurringTransaction recurringTransaction = new RecurringTransaction();
        recurringTransaction.setTransaction(transaction);
        recurringTransaction.setPaid_date(transaction.getDate());
        recurringTransaction.setActive(true);
        recurringTransaction.setCreated_date(LocalDateTime.now());
        recurringTransactionRepository.save(recurringTransaction);
    }

    private void handleRecurringTransactionUpdate(Transaction transaction, boolean wasRecurring, boolean nowRecurring) {
        if (!wasRecurring && nowRecurring) {
            createRecurringTransaction(transaction);
        } else if (wasRecurring && !nowRecurring) {
            recurringTransactionRepository.deleteById(transaction.getId());
        }
    }
}
