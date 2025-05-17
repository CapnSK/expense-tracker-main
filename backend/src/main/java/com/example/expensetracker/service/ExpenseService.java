package com.example.expensetracker.service;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Page<Expense> getExpenses(String keyword, String category, int page, int size, String[] sort) {
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        if (keyword != null && !keyword.isEmpty() && category != null && !category.isEmpty()) {
            return expenseRepository.findByDescriptionContainingIgnoreCaseAndCategory(keyword, category, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            return expenseRepository.findByDescriptionContainingIgnoreCase(keyword, pageable);
        } else if (category != null && !category.isEmpty()) {
            return expenseRepository.findByCategory(category, pageable);
        } else {
            return expenseRepository.findAll(pageable);
        }
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    public Expense updateExpense(Long id, Expense expense) {
        Expense existing = getExpenseById(id);
        if (existing != null) {
            existing.setDescription(expense.getDescription());
            existing.setAmount(expense.getAmount());
            existing.setDate(expense.getDate());
            return expenseRepository.save(existing);
        }
        return null;
    }

    public void deleteExpense(Optional<Long> id) {
        if(id.isEmpty()){
            expenseRepository.deleteAll();
            return; 
        }
        expenseRepository.deleteById(id.get());
    }
}