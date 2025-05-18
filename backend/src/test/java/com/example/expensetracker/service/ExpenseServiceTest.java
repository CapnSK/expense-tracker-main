package com.example.expensetracker.service;

import com.example.expensetracker.exceptions.InvalidExpenseException;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private Expense sampleExpense;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleExpense = new Expense();
        sampleExpense.setId(1L);
        sampleExpense.setDescription("Lunch");
        sampleExpense.setAmount(100.0);
        sampleExpense.setDate(LocalDate.now());
    }

    @Test
    void testCreateExpense_Valid() {
        when(expenseRepository.save(any())).thenReturn(sampleExpense);
        Expense result = expenseService.createExpense(sampleExpense);
        assertEquals(sampleExpense.getDescription(), result.getDescription());
    }

    @Test
    void testCreateExpense_InvalidAmount() {
        sampleExpense.setAmount(0.0);
        assertThrows(InvalidExpenseException.class, () -> expenseService.createExpense(sampleExpense));
    }

    @Test
    void testCreateExpense_FutureDate() {
        sampleExpense.setDate(LocalDate.now().plusDays(1));
        assertThrows(InvalidExpenseException.class, () -> expenseService.createExpense(sampleExpense));
    }

    @Test
    void testGetAllExpenses() {
        when(expenseRepository.findAll()).thenReturn(List.of(sampleExpense));
        List<Expense> result = expenseService.getAllExpenses();
        assertEquals(1, result.size());
    }

    @Test
    void testGetExpenseById_Found() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(sampleExpense));
        Expense result = expenseService.getExpenseById(1L);
        assertNotNull(result);
    }

    @Test
    void testGetExpenseById_NotFound() {
        when(expenseRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(expenseService.getExpenseById(2L));
    }

    @Test
    void testUpdateExpense_Found() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(sampleExpense));
        when(expenseRepository.save(any())).thenReturn(sampleExpense);

        Expense updated = new Expense();
        updated.setDescription("Dinner");
        updated.setAmount(150.0);
        updated.setDate(LocalDate.now());

        Expense result = expenseService.updateExpense(1L, updated);
        assertEquals("Dinner", result.getDescription());
    }

    @Test
    void testUpdateExpense_NotFound() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.empty());
        Expense result = expenseService.updateExpense(1L, sampleExpense);
        assertNull(result);
    }

    @Test
    void testDeleteExpense_ById() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(sampleExpense));
        expenseService.deleteExpense(Optional.of(1L));
        assertTrue(sampleExpense.isDeleted());
        verify(expenseRepository).save(sampleExpense);
    }

    @Test
    void testDeleteExpense_All() {
        expenseService.deleteExpense(Optional.empty());
        verify(expenseRepository).deleteAll();
    }

    @Test
    void testArchiveOldExpenses() {
        List<Expense> oldExpenses = List.of(sampleExpense);
        when(expenseRepository.findByDateBeforeAndDeletedFalse(any())).thenReturn(oldExpenses);
        expenseService.archiveOldExpenses();
        verify(expenseRepository).saveAll(oldExpenses);
        assertTrue(sampleExpense.isDeleted());
    }

    @Test
    void testGetExpenses_withFilters() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("date").descending());
        Page<Expense> mockPage = new PageImpl<>(List.of(sampleExpense));
        when(expenseRepository.searchAndFilter(null, null, false, pageable)).thenReturn(mockPage);
        Page<Expense> result = expenseService.getExpenses("", "", 0, 5, new String[]{"date", "desc"}, false);
        assertEquals(1, result.getTotalElements());
    }
}
