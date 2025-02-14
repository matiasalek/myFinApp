package com.myfinapp.service;

import com.myfinapp.exception.ResourceNotFoundException;
import com.myfinapp.model.Category;
import com.myfinapp.model.Transaction;
import com.myfinapp.repository.CategoryRepository;
import com.myfinapp.repository.TransactionRepository;
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
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
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

        if (transaction.getCategory() == null || transaction.getCategory().getName() == null) {
            throw new ResourceNotFoundException("Invalid category");
        }

        Category existingCategory = categoryRepository.findByName(transaction.getCategory().getName())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + transaction.getCategory().getName()));

        transaction.setCategory(existingCategory);

        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        return transactionRepository.findById(id).map(transaction -> {
            transaction.setCategory(transactionDetails.getCategory());
            transaction.setDescription(transactionDetails.getDescription());
            transaction.setAmount(transactionDetails.getAmount());
            transaction.setDate(transactionDetails.getDate());
            transaction.setRecurring(transactionDetails.getRecurring());
            return transactionRepository.save(transaction);
        }).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    public Transaction patchTransaction(Long id, Map<String, Object> updates) {
        return transactionRepository.findById(id).map(transaction -> {
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
                        transaction.setRecurring((Boolean) entry.getValue());
                        break;
                }
            }

            return transactionRepository.save(transaction);
        }).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found");
        }
        transactionRepository.deleteById(id);
    }
}
