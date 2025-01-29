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

    public List<Transactions> getAllFins(){
        return transactionsRepository.findAll();
    }

    public Transactions createFin(Transactions transactions){
        return transactionsRepository.save(transactions);
    }

    public Transactions updateFin(Long id, Transactions transactionsDetails) {
        return transactionsRepository.findById(id).map(transactions -> {
            transactions.setTrackDesc(transactionsDetails.getTrack_desc());
            transactions.setCategory(transactionsDetails.getCategory());
            transactions.setValue(transactionsDetails.getValue());
            transactions.setDate(transactionsDetails.getDate());
            return transactionsRepository.save(transactions);
        }).orElseThrow(()-> new RuntimeException("Fin not found"));
    }

    public void deleteFin(Long id){
        Transactions transactions = transactionsRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Fin not found"));
        transactionsRepository.delete(transactions);
    }
}
