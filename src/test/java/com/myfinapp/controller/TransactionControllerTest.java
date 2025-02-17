package com.myfinapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myfinapp.exception.GlobalExceptionHandler;
import com.myfinapp.exception.ResourceNotFoundException;
import com.myfinapp.model.Category;
import com.myfinapp.model.Transaction;
import com.myfinapp.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.myfinapp.model.Category.categories.OTHER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllTransactions_ShouldReturnAllTransactions() throws Exception {
        Long transactionId = 3L;
        LocalDate now = LocalDate.now();
        Category testCategory = new Category(3L, OTHER);
        Transaction expectedTransaction = new Transaction(3L, testCategory, "credit card", new BigDecimal("1.1"), now, false);


        when(transactionService.getTransactionById(transactionId))
                .thenReturn(expectedTransaction);

        mockMvc.perform(get("/api/transaction/" + transactionId))
                .andExpect(status().isOk());
    }

    @Test
    void createTransaction_ShouldCreateTransaction() throws Exception {
        LocalDate now = LocalDate.now();
        Long transactionId = 3L;
        Category testCategory = new Category(3L, OTHER);
        Transaction testTransaction = new Transaction(transactionId, testCategory, "credit card", new BigDecimal("1.1"), now, false);

        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(testTransaction);

        mockMvc.perform(post("/api/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(testTransaction)))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteTransaction_ShouldDeleteTransaction() throws Exception {
        Long transactionId = 1L;

        mockMvc.perform(delete("/api/transaction/" + transactionId))
                .andExpect(status().isNoContent());

        verify(transactionService, times(1)).deleteTransaction(transactionId);
    }

    @Test
    void deleteTransaction_WhenTransactionNotFound_ShouldReturnNotFound() throws Exception {
        Long transactionId = 999L;

        doThrow(new ResourceNotFoundException("Transaction not found"))
                .when(transactionService).deleteTransaction(transactionId);

        mockMvc.perform(delete("/api/transaction/" + transactionId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Transaction not found"));
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