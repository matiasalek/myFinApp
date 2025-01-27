package com.myfinapp.service;

import com.myfinapp.model.Fin;
import com.myfinapp.repository.FinRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinService {
    private final FinRepository finRepository;

    public FinService(FinRepository finRepository){
        this.finRepository = finRepository;
    }

    public List<Fin> getAllFins(){
        return finRepository.findAll();
    }

    public Fin createFin(Fin fin){
        return finRepository.save(fin);
    }

    public Fin updateFin(Long id,Fin finDetails) {
        return finRepository.findById(id).map(fin -> {
            fin.setTrackDesc(finDetails.getTrackDesc());
            fin.setCategory(finDetails.getCategory());
            fin.setValue(finDetails.getValue());
            fin.setDate(finDetails.getDate());
            return finRepository.save(fin);
        }).orElseThrow(()-> new RuntimeException("Fin not found"));
    }

    public void deleteFin(Long id){
        Fin fin = finRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Fin not found"));
        finRepository.delete(fin);
    }
}
