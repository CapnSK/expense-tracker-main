package com.example.expensetracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.expensetracker.config.TestSecurityConfig;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.service.ExpenseService;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestSecurityConfig.class)
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseService expenseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllExpenses() throws Exception {
        List<Expense> expenses = Arrays.asList(
                new Expense(1L, "Groceries", 50.0, "Food", null, false),
                new Expense(2L, "Taxi", 20.0, "Transport", null, false));

        Mockito.when(expenseService.getAllExpenses()).thenReturn(expenses);

        mockMvc.perform(get("/api/expenses/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    public void testCreateExpense() throws Exception {
        Expense newExpense = new Expense(null, "Coffee", 5.0, "Food", null, false);
        Expense savedExpense = new Expense(1L, "Coffee", 5.0, "Food", null, false);

        Mockito.when(expenseService.createExpense(any(Expense.class))).thenReturn(savedExpense);

        mockMvc.perform(post("/api/expenses/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newExpense)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Coffee"));
    }

    @Test
    public void testDeleteExpenseById() throws Exception {
        mockMvc.perform(delete("/api/expenses/1"))
                .andExpect(status().isOk());

        Mockito.verify(expenseService).deleteExpense(Optional.of(1L));
    }

    @Test
    public void testGetExpensesWithPagination() throws Exception {
        List<Expense> pageContent = List.of(
                new Expense(1L, "Lunch", 15.0, "Food", null, false));
        PageImpl<Expense> page = new PageImpl<>(pageContent, PageRequest.of(0, 10), 1);

        Mockito.when(expenseService.getExpenses(anyString(), anyString(), anyInt(), anyInt(), any(), anyBoolean()))
                .thenReturn(page);

        mockMvc.perform(get("/api/expenses")
                .param("keyword", "")
                .param("category", "")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "date,desc")
                .param("archived", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].description").value("Lunch"));
    }

    @Test
    public void testGetExpenseById() throws Exception {
        Expense mockExpense = new Expense();
        mockExpense.setId(1L);
        mockExpense.setDescription("Mock Expense");
        mockExpense.setAmount(100.0);
        mockExpense.setCategory("Food");
        mockExpense.setDeleted(false);

        Mockito.when(expenseService.getExpenseById(1L)).thenReturn(mockExpense);

        mockMvc.perform(get("/api/expenses/u/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Mock Expense"))
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.category").value("Food"))
                .andExpect(jsonPath("$.deleted").value(false));
    }

    @Test
    public void testUpdateExpense() throws Exception {
        Expense updatedExpense = new Expense();
        updatedExpense.setId(1L);
        updatedExpense.setDescription("Updated");
        updatedExpense.setAmount(300.0);
        updatedExpense.setCategory("Bills");
        updatedExpense.setDeleted(false);

        Mockito.when(expenseService.updateExpense(Mockito.eq(1L), Mockito.any(Expense.class)))
                .thenReturn(updatedExpense);

        mockMvc.perform(put("/api/expenses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedExpense)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated"))
                .andExpect(jsonPath("$.amount").value(300.0))
                .andExpect(jsonPath("$.category").value("Bills"))
                .andExpect(jsonPath("$.deleted").value(false));
    }

    @Test
    public void testDeleteAllExpenses() throws Exception {
        // Nothing to mock here since deleteExpense(Optional.empty()) returns void
        mockMvc.perform(delete("/api/expenses/all"))
                .andExpect(status().isOk());

        Mockito.verify(expenseService).deleteExpense(Optional.empty());
    }

}
