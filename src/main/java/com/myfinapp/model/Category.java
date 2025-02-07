package com.myfinapp.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private categories name;

    @Column(nullable = false)
    private Timestamp date;

    public enum categories{
        MISC,
        CREDIT_CARD,
        RECURRING_SPENDS,
        SAVING
    }

    // Empty constructor for JPA
    public Category() {
    }

    // Full constructor for easy testing
    public Category(Long id, categories name, Timestamp date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public categories getName() {
        return name;
    }

    public void setName(categories name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}