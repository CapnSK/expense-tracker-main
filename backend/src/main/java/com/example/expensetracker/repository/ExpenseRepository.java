package com.example.expensetracker.repository;

import com.example.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findByDescription(String keyword, Pageable pageable);

    Page<Expense> findByDescriptionContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Expense> findByCategory(String category, Pageable pageable);

    Page<Expense> findByDescriptionContainingIgnoreCaseAndCategory(String keyword, String category, Pageable pageable);

    @Query("SELECT e FROM Expense e WHERE e.deleted = :deleted " +
            "AND (:keyword IS NULL OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:category IS NULL OR e.category = :category)")
    Page<Expense> searchAndFilter(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("deleted") Boolean deleted,
            Pageable pageable);
    
    Page<Expense> findAllByDeletedTrue(Pageable pageable);
    Page<Expense> findAllByDeletedFalse(Pageable pageable);
    List<Expense> findByDateBeforeAndDeletedFalse(LocalDate cutoffDate);
}
