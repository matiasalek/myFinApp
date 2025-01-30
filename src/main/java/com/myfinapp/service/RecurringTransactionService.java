package com.myfinapp.service;

import com.myfinapp.model.RecurringTransaction;
import com.myfinapp.repository.RecurringTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return recurringTransactionRepository.findById(id).orElseThrow(()->new RuntimeException("Transaction not found"));
    }


    public RecurringTransaction createRecurringTransaction(RecurringTransaction recurringTransaction){
        return recurringTransactionRepository.save(recurringTransaction);
    }


    //how to handle ids?
    public RecurringTransaction updateRecurringTransaction(Long id, RecurringTransaction recurringTransactionDetails){
        return recurringTransactionRepository.findById(id).map(recurringTransaction -> {
            recurringTransaction.setCreated_date(recurringTransactionDetails.getCreated_date());
            recurringTransaction.setActive(recurringTransactionDetails.isActive());
            return recurringTransactionRepository.save(recurringTransaction);
        }).orElseThrow(()->new RuntimeException("Recurrent Transaction not found"));
    }

    public void deleteRecurringTransaction(Long id){
        RecurringTransaction recurringTransaction = recurringTransactionRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Recurrent Transaction not found"));
        recurringTransactionRepository.delete(recurringTransaction);
    }
}
