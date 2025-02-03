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
    @Column(name = "name")
    private categories name;

    private Timestamp date;

    public enum categories{
        FOOD,
        CREDIT_CARD,
        RECURRING_SPENDS,
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