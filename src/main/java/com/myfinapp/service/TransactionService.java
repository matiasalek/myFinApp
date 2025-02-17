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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
                            try {
                                Object dateValue = entry.getValue();
                                String dateString = dateValue.toString().trim();
                                try {
                                    // Standard ISO format
                                    if (dateString.contains("T")) {
                                        dateString = dateString.split("T")[0];
                                    }

                                    // Basic check for yyyy-MM-dd format
                                    if (dateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
                                        transaction.setDate(LocalDate.parse(dateString));
                                    } else {
                                        throw new DateTimeParseException("Invalid date format", dateString, 0);
                                    }
                                } catch (DateTimeParseException e) {
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                    transaction.setDate(LocalDate.parse(dateString, formatter));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new IllegalArgumentException("Invalid date format: " + entry.getValue() +
                                        ". Expected format: yyyy-MM-dd. Error: " + e.getMessage());
                            }
                            break;
                        case "recurring":
                            try {
                                Object value = entry.getValue();
                                boolean boolValue = switch (value) {
                                    case Boolean b -> b;
                                    case String s -> Boolean.parseBoolean(s);
                                    case Number number -> number.intValue() != 0;
                                    case null, default -> throw new IllegalArgumentException("Invalid boolean format");
                                };
                                transaction.setRecurring(boolValue);
                            } catch (Exception e) {
                                throw new IllegalArgumentException("Invalid recurring value: " + entry.getValue());
                            }
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
}