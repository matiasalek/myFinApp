package com.myfinapp.controller;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myfinapp.exception.GlobalExceptionHandler;
import com.myfinapp.exception.ResourceNotFoundException;
import com.myfinapp.model.Category;
import com.myfinapp.model.RecurringTransaction;
import com.myfinapp.model.Transaction;
import com.myfinapp.service.RecurringTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static com.myfinapp.model.Category.categories.MISC;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class RecurringTransactionControllerTest {
    @Mock
    private RecurringTransactionService recurringTransactionService;

    @InjectMocks
    private RecurringTransactionController recurringTransactionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recurringTransactionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllRecurringTransactions_ShouldReturnAllRecurringTransactions() throws Exception {
        Long recurringTransactionId = 3L;
        LocalDateTime now = LocalDateTime.now();

        Category testCategory = new Category(3L, MISC, now);
        Transaction testTransaction = new Transaction(3L, testCategory, "credit card", new BigDecimal("1.1"), now, false, null);
        RecurringTransaction expectedRecurringTransaction = new RecurringTransaction(
                recurringTransactionId,
                testTransaction,
                now,
                true,
                now.minusDays(20L)
        );

        when(recurringTransactionService.getAllRecurringTransaction())
                .thenReturn(Arrays.asList(expectedRecurringTransaction));

        mockMvc.perform(get("/api/recurringtransaction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(recurringTransactionId))
                .andExpect(jsonPath("$[0].transaction.description").value("credit card"))
                .andExpect(jsonPath("$[0].transaction.amount").value("1.1"))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    void createRecurringTransaction_ShouldCreateRecurringTransaction() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Long recurringTransactionId = 3L;

        Category testCategory = new Category(3L, MISC, now);
        Transaction testTransaction = new Transaction(null, testCategory, "credit card", new BigDecimal("1.1"), now, false, null);

        RecurringTransaction inputRecurringTransaction = new RecurringTransaction(
                null,
                testTransaction,
                now,
                true,
                null
        );

        RecurringTransaction savedRecurringTransaction = new RecurringTransaction(
                recurringTransactionId,
                new Transaction(3L, testCategory, "credit card", new BigDecimal("1.1"), now, false, null),
                now,
                true,
                null
        );

        when(recurringTransactionService.createRecurringTransaction(any(RecurringTransaction.class)))
                .thenReturn(savedRecurringTransaction);

        mockMvc.perform(post("/api/recurringtransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputRecurringTransaction)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(recurringTransactionId))
                .andExpect(jsonPath("$.transaction.description").value("credit card"))
                .andExpect(jsonPath("$.transaction.amount").value("1.1"))
                .andExpect(jsonPath("$.active").value(true));
    }


    @Test
    void updateRecurringTransaction_ShouldUpdateRecurringTransaction() throws Exception {
        Long recurringTransactionId = 1L;
        LocalDateTime now = LocalDateTime.now();

        Category testCategory = new Category(1L, MISC, now);
        Transaction testTransaction = new Transaction(1L, testCategory, "updated credit card", new BigDecimal("2.2"), now, false, null);

        RecurringTransaction updatedRecurringTransaction = new RecurringTransaction(
                recurringTransactionId,
                testTransaction,
                now,
                false,  // changed active status
                now
        );

        when(recurringTransactionService.updateRecurringTransaction(eq(recurringTransactionId), any(RecurringTransaction.class)))
                .thenReturn(updatedRecurringTransaction);

        mockMvc.perform(put("/api/recurringtransaction/" + recurringTransactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedRecurringTransaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recurringTransactionId))
                .andExpect(jsonPath("$.transaction.description").value("updated credit card"))
                .andExpect(jsonPath("$.transaction.amount").value("2.2"))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void updateRecurringTransaction_WhenRecurringTransactionNotFound_ShouldReturnNotFound() throws Exception {
        Long recurringTransactionId = 999L;
        LocalDateTime now = LocalDateTime.now();
        Category testCategory = new Category(999L, MISC, now);
        Transaction testTransaction = new Transaction(999L, testCategory, "credit card", new BigDecimal("1.1"), now, false, null);

        RecurringTransaction updatedRecurringTransaction = new RecurringTransaction(
                recurringTransactionId,
                testTransaction,
                now,
                true,
                now.minusDays(20L)
        );

        when(recurringTransactionService.updateRecurringTransaction(eq(recurringTransactionId), any(RecurringTransaction.class)))
                .thenThrow(new ResourceNotFoundException("Recurring Transaction not found"));

        mockMvc.perform(put("/api/recurringtransaction/" + recurringTransactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedRecurringTransaction)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Recurring Transaction not found"));
    }

    @Test
    void deleteRecurringTransaction_ShouldDeleteRecurringTransaction() throws Exception {
        Long recurringTransactionId = 1L;

        mockMvc.perform(delete("/api/recurringtransaction/" + recurringTransactionId))
                .andExpect(status().isNoContent());

        verify(recurringTransactionService, times(1)).deleteRecurringTransaction(recurringTransactionId);
    }

    @Test
    void deleteRecurringTransaction_WhenRecurringTransactionNotFound_ShouldReturnNotFound() throws Exception {
        Long recurringTransactionId = 999L;

        doThrow(new ResourceNotFoundException("Recurring Transaction not found"))
                .when(recurringTransactionService).deleteRecurringTransaction(recurringTransactionId);

        mockMvc.perform(delete("/api/recurringtransaction/" + recurringTransactionId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Recurring Transaction not found"));
    }

    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}