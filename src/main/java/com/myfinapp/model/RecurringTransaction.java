package com.myfinapp.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "recurring_transactions")
public class RecurringTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(nullable = false)
    private LocalDateTime paid_date;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private LocalDateTime created_date;

    // Empty constructor for JPA
    public RecurringTransaction() {
    }

    // Full constructor for easy testing
    public RecurringTransaction(Long id, Transaction transaction, LocalDateTime paid_date, boolean active, LocalDateTime created_date) {
        this.id = id;
        this.transaction = transaction;
        this.paid_date = paid_date;
        this.active = active;
        this.created_date = created_date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public LocalDateTime getPaid_date() {
        return paid_date;
    }

    public void setPaid_date(LocalDateTime paid_date) {
        this.paid_date = paid_date;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreated_date() {
        return created_date;
    }

    public void setCreated_date(LocalDateTime created_date) {
        this.created_date = created_date;
    }
}