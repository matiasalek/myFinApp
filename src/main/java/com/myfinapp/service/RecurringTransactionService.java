package com.myfinapp.service;

import com.myfinapp.exception.ResourceNotFoundException;
import com.myfinapp.model.RecurringTransaction;
import com.myfinapp.repository.RecurringTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class RecurringTransactionService {
    private final RecurringTransactionRepository recurringTransactionRepository;

    public RecurringTransactionService(RecurringTransactionRepository recurringTransactionRepository){
        this.recurringTransactionRepository = recurringTransactionRepository;
    }

    @Transactional(readOnly = true)
    public List<RecurringTransaction> getAllRecurringTransaction(){
        return recurringTransactionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public RecurringTransaction getRecurringTransactionById(Long id) {
        return recurringTransactionRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Transaction not found"));
    }

    public RecurringTransaction createRecurringTransaction(RecurringTransaction recurringTransaction){
        return recurringTransactionRepository.save(recurringTransaction);
    }

    public RecurringTransaction updateRecurringTransaction(Long id, RecurringTransaction recurringTransactionDetails){
        return recurringTransactionRepository.findById(id).map(recurringTransaction -> {
            recurringTransaction.setCreated_date(recurringTransactionDetails.getCreated_date());
            recurringTransaction.setActive(recurringTransactionDetails.isActive());
            return recurringTransactionRepository.save(recurringTransaction);
        }).orElseThrow(()->new ResourceNotFoundException("Recurrent Transaction not found"));
    }

    public RecurringTransaction patchRecurringTransaction(Long id, Map<String, Object> updates) {
        return recurringTransactionRepository.findById(id).map(recurringTransaction -> {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "paid_date":
                        recurringTransaction.setPaid_date((LocalDateTime) value);
                        break;
                    case "created_date":
                        recurringTransaction.setCreated_date((LocalDateTime) value);
                        break;
                    case "active":
                        recurringTransaction.setActive((Boolean) value);
                }
            });
            return recurringTransactionRepository.save(recurringTransaction);
        }).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    public void deleteRecurringTransaction(Long id){
        RecurringTransaction recurringTransaction = recurringTransactionRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Recurrent Transaction not found"));
        recurringTransactionRepository.delete(recurringTransaction);
    }
}