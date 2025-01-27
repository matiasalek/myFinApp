package com.myfinapp.repository;

import com.myfinapp.model.Fin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinRepository extends JpaRepository<Fin, Long> {
}
