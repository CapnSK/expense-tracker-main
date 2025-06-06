package com.example.expensetracker.model;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

    @Column(columnDefinition = "varchar(255) default 'HOUSEHOLD'")
    private String category = "HOUSEHOLD";

    private Double amount;
    private LocalDate date;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;

    // Constructors
    public Expense() {
    }

    public Expense(String description, Double amount, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
    }
    
    public Expense(Long id, String description, Double amount, String category, Date date, Boolean archived) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date == null ? null : LocalDate.ofEpochDay(date.getTime());
        this.deleted = archived;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
