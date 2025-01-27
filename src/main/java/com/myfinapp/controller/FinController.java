package com.myfinapp.controller;

import com.myfinapp.model.Fin;
import com.myfinapp.service.FinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/myfin")
public class FinController {

    private final FinService finService;

    // Constructor-based dependency injection
    public FinController(FinService finService) {
        this.finService = finService;
    }

    @GetMapping
    public List<Fin> getAllFins() {
        return finService.getAllFins();
    }

    @PostMapping
    public ResponseEntity<Fin> createFin(@RequestBody Fin fin) {
        Fin createdFin = finService.createFin(fin);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFin);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fin> updateFin(@PathVariable Long id, @RequestBody Fin finDetails) {
        Fin updatedFin = finService.updateFin(id, finDetails);
        return ResponseEntity.ok(updatedFin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFin(@PathVariable Long id) {
        finService.deleteFin(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content when successful
    }
}