package com.myfinapp.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "recurring_transactions")
public class RecurringTransactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transactions transaction;
    private Timestamp paid_date;
    private boolean active;
    private Timestamp created_date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transactions getTransaction() {
        return transaction;
    }

    public void setTransaction(Transactions transaction) {
        this.transaction = transaction;
    }

    public Timestamp getPaid_date() {
        return paid_date;
    }

    public void setPaid_date(Timestamp paid_date) {
        this.paid_date = paid_date;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }
}
