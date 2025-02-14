package com.myfinapp.repository;

import com.myfinapp.model.Category;
import com.myfinapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.category = :category AND t.date BETWEEN :startDate AND :endDate")
    Optional<BigDecimal> sumAmountByCategoryAndDateRange(@Param("category") Category category,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);
}