package com.myfinapp.service;

import com.myfinapp.exception.ResourceNotFoundException;
import com.myfinapp.model.Transaction;
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

    public TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Transaction not found"));
    }

    public Transaction createTransaction(Transaction transaction){
        if (transaction.getId() != null) {
            throw new ResourceNotFoundException("Transaction already exists");
        }
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found");
        }
        return transactionRepository.findById(id).map(transaction -> {
            transaction.setCategory(transactionDetails.getCategory());
            transaction.setDescription(transactionDetails.getDescription());
            transaction.setAmount(transactionDetails.getAmount());
            transaction.setDate(transactionDetails.getDate());
            transaction.setRecurring(transactionDetails.isRecurring());
            return transactionRepository.save(transaction);
        })
                .orElseThrow(()-> new ResourceNotFoundException("Transaction not found"));
    }

    public Transaction patchTransaction(Long id, Map<String, Object> updates){
        return transactionRepository.findById(id).map(transaction -> {
            updates.forEach((key, value) ->{
                switch (key) {
                    case "description":
                        transaction.setDescription((String) value);
                        break;
                    case "amount":
                        transaction.setAmount((BigDecimal) value);
                        break;
                    case "date":
                        transaction.setDate((LocalDateTime) value);
                        break;
                    case "recurring":
                        transaction.setRecurring((Boolean) value);
                        break;
                }
            });
            return transactionRepository.save(transaction);
        }).orElseThrow(()-> new ResourceNotFoundException("Transaction not found"));
    }

    public void deleteTransaction(Long id){
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found");
        }
        transactionRepository.deleteById(id);
    }
}