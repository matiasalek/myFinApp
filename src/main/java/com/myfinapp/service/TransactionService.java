package com.myfinapp.service;

import com.myfinapp.exception.ResourceNotFoundException;
import com.myfinapp.model.Category;
import com.myfinapp.model.Transaction;
import com.myfinapp.repository.CategoryRepository;
import com.myfinapp.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        try {
            return transactionRepository.findById(id).map(transaction -> {
                for (Map.Entry<String, Object> entry : updates.entrySet()) {
                    switch (entry.getKey()) {
                        case "description":
                            transaction.setDescription(entry.getValue().toString());
                            break;
                        case "amount":
                            try {
                                if (entry.getValue() instanceof Number) {
                                    transaction.setAmount(new BigDecimal(entry.getValue().toString()));
                                } else if (entry.getValue() instanceof String) {
                                    transaction.setAmount(new BigDecimal((String) entry.getValue()));
                                } else {
                                    throw new IllegalArgumentException("Invalid amount format");
                                }
                            } catch (NumberFormatException e) {
                                throw new IllegalArgumentException("Invalid amount value: " + entry.getValue());
                            }
                            break;
                        case "date":
                            transaction.setDate(LocalDate.parse(entry.getValue().toString()));
                            break;
                        case "recurring":
                            transaction.setRecurring(Boolean.parseBoolean(entry.getValue().toString()));
                            break;
                        case "category":
                            if (entry.getValue() instanceof Map) {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> categoryMap = (Map<String, Object>) entry.getValue();
                                try {
                                    String categoryName = categoryMap.get("id").toString()
                                            .toUpperCase()
                                            .replace(" ", "_");

                                    Category.categories enumCategory = Category.categories.valueOf(categoryName);
                                    Category category = categoryRepository.findByName(enumCategory)
                                            .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + categoryName));

                                    transaction.setCategory(category);
                                } catch (IllegalArgumentException e) {
                                    throw new IllegalArgumentException("Invalid category name: " + categoryMap.get("id"));
                                }
                            }
                            break;
                        case "id":
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid field: " + entry.getKey());
                    }
                }

                return transactionRepository.save(transaction);
            }).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating transaction: " + e.getMessage());
        }
    }



    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found");
        }
        transactionRepository.deleteById(id);
    }

    public BigDecimal getTotalAmountByCategoryAndDateRange(Category.categories categoryName, LocalDate startDate, LocalDate endDate){
        Optional<Category> category = categoryRepository.findByName(categoryName);
        if (category.isEmpty()){
            throw new ResourceNotFoundException("Category not found: " + categoryName);
        }

        return transactionRepository.sumAmountByCategoryAndDateRange(category.get(), startDate, endDate)
                .orElse(BigDecimal.ZERO);
    }
}