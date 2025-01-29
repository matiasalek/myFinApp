package com.myfinapp.repository;

import com.myfinapp.model.RecurringTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurringTransactionsRepository extends JpaRepository<RecurringTransactions, Long> {
}
