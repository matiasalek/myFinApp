package com.myfinapp.service;

import com.myfinapp.model.Transactions;
import com.myfinapp.repository.TransactionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionsService {
    private final TransactionsRepository transactionsRepository;

    public TransactionsService(TransactionsRepository transactionsRepository){
        this.transactionsRepository = transactionsRepository;
    }

    public List<Transactions> getAllTransactions(){
        return transactionsRepository.findAll();
    }

    public Transactions createTransaction(Transactions transactions){
        return transactionsRepository.save(transactions);
    }

    public Transactions updateTransaction(Long id, Transactions transactionsDetails) {
        return transactionsRepository.findById(id).map(transactions -> {
            transactions.setDescription(transactionsDetails.getDescription());
            transactions.setAmount(transactionsDetails.getAmount());
            transactions.setDate(transactionsDetails.getDate());
            transactions.setIs_recurring(transactionsDetails.isIs_recurring());
            return transactionsRepository.save(transactions);
        }).orElseThrow(()-> new RuntimeException("Transaction not found"));
    }

    public void deleteTransaction(Long id){
        Transactions transactions = transactionsRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Transaction not found"));
        transactionsRepository.delete(transactions);
    }
}
