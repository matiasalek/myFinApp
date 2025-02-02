package com.myfinapp.service;

import com.myfinapp.model.Transaction;
import com.myfinapp.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.RuntimeErrorException;
import java.util.List;

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
        return transactionRepository.findById(id).orElseThrow(()->new RuntimeException("Transaction not found"));
    }


    public Transaction createTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public Transaction updateFullTransaction(Long id, Transaction transactionDetails) {
        return transactionRepository.findById(id).map(transaction -> {
            transaction.setCategory(transactionDetails.getCategory());
            transaction.setDescription(transactionDetails.getDescription());
            transaction.setAmount(transactionDetails.getAmount());
            transaction.setDate(transactionDetails.getDate());
            transaction.setRecurring(transactionDetails.isRecurring());
            return transactionRepository.save(transaction);
        }).orElseThrow(()-> new RuntimeException("Transaction not found"));
    }

    public Transaction updateCategoryTransaction(Long id, Transaction transactionDetails) {
        return transactionRepository.findById(id).map(transaction -> {
            transaction.setCategory(transactionDetails.getCategory());
            return transactionRepository.save(transaction);
        }).orElseThrow(()-> new RuntimeException("Transaction not found"));
    }

    public Transaction updateDescriptionTransaction(Long id, Transaction transactionDetails) {
        return transactionRepository.findById(id).map(transaction -> {
            transaction.setDescription(transactionDetails.getDescription());
            return transactionRepository.save(transaction);
        }).orElseThrow(()-> new RuntimeException("Transaction not found"));
    }

    public Transaction updateAmountTransaction(Long id, Transaction transactionDetails) {
        return transactionRepository.findById(id).map(transaction -> {
            transaction.setAmount(transactionDetails.getAmount());
            return transactionRepository.save(transaction);
        }).orElseThrow(()-> new RuntimeException("Transaction not found"));
    }

    public Transaction updateDateTransaction(Long id, Transaction transactionDetails) {
        return transactionRepository.findById(id).map(transaction -> {
            transaction.setDate(transactionDetails.getDate());
            return transactionRepository.save(transaction);
        }).orElseThrow(()-> new RuntimeException("Transaction not found"));
    }

    public Transaction updateRecurringTransaction(Long id, Transaction transactionDetails) {
        return transactionRepository.findById(id).map(transaction -> {
            transaction.setRecurring(transactionDetails.isRecurring());
            return transactionRepository.save(transaction);
        }).orElseThrow(()-> new RuntimeException("Transaction not found"));
    }

    public void deleteTransaction(Long id){
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Transaction not found"));
        transactionRepository.delete(transaction);
    }
}