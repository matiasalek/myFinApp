package com.myfinapp.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private boolean recurring;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecurringTransaction> recurringTransactionList;

    // Empty constructor for JPA
    public Transaction() {
    }

    // Full constructor for easy testing
    public Transaction(Long id, Category category, String description, BigDecimal amount, LocalDateTime date, boolean recurring, List<RecurringTransaction> recurringTransactionList) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.recurring = recurring;
        this.recurringTransactionList = recurringTransactionList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public List<RecurringTransaction> getRecurringTransactionList() {
        return recurringTransactionList;
    }

    public void setRecurringTransactionList(List<RecurringTransaction> recurringTransactionList) {
        this.recurringTransactionList = recurringTransactionList;
    }
}