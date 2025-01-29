package com.myfinapp.service;

import com.myfinapp.model.RecurringTransactions;
import com.myfinapp.repository.RecurringTransactionsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class RecurringTransactionsService {
    private final RecurringTransactionsRepository recurringTransactionsRepository;

    public RecurringTransactionsService(RecurringTransactionsRepository recurringTransactionsRepository){
        this.recurringTransactionsRepository = recurringTransactionsRepository;
    }

    @Transactional(readOnly = true)
    public List<RecurringTransactions> getAllRecurringTransactions(){
        return recurringTransactionsRepository.findAll();
    }

    public RecurringTransactions createRecurringTransactions(RecurringTransactions recurringTransactions){
        return recurringTransactionsRepository.save(recurringTransactions);
    }


    //how to handle ids?
    public RecurringTransactions updateRecurringTransactions(Long id, RecurringTransactions recurringTransactionsDetails){
        return recurringTransactionsRepository.findById(id).map(recurringTransactions -> {
            recurringTransactions.setCreated_date(recurringTransactionsDetails.getCreated_date());
            recurringTransactions.setActive(recurringTransactionsDetails.isActive());
            return recurringTransactionsRepository.save(recurringTransactions);
        }).orElseThrow(()->new RuntimeException("Recurrent Transaction not found"));
    }

    public void deleteRecurringTransaction(Long id){
        RecurringTransactions recurringTransactions = recurringTransactionsRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Recurrent Transaction not found"));
        recurringTransactionsRepository.delete(recurringTransactions);
    }
}
