package com.myfinapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private categories name;

    public enum categories{
        OTHER,
        CREDIT_CARD,
        RECREATION,
        SAVING,
        UNIVERSITY
    }

    // Empty constructor for JPA
    public Category() {
    }

    // Full constructor for easy testing
    public Category(Long id, categories name) {
        this.id = id;
        this.name = name;
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