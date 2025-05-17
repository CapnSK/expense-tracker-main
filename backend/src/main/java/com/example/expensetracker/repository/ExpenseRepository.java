package com.example.expensetracker.repository;

import com.example.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findByDescription(String keyword, Pageable pageable);
    Page<Expense> findByDescriptionContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Expense> findByCategory(String category, Pageable pageable);
    Page<Expense> findByDescriptionContainingIgnoreCaseAndCategory(String keyword, String category, Pageable pageable);
}
