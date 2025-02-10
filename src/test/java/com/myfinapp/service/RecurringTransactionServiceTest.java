package com.myfinapp.service;

import com.myfinapp.exception.ResourceNotFoundException;
import com.myfinapp.model.Category;
import com.myfinapp.model.RecurringTransaction;
import com.myfinapp.model.Transaction;
import com.myfinapp.repository.RecurringTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecurringTransactionServiceTest {

    @Mock
    private RecurringTransactionRepository recurringTransactionRepository;

    @InjectMocks
    private RecurringTransactionService recurringTransactionService;

    private RecurringTransaction sampleRecurringTransaction;

    @BeforeEach
    void setUp() {
        Category sampleCategory = new Category(1L, Category.categories.CREDIT_CARD, LocalDateTime.now());
        Transaction sampleTransaction = new Transaction
                (1L, sampleCategory, "food", new BigDecimal("1.10"), LocalDateTime.now(), false, null);
        sampleRecurringTransaction = new RecurringTransaction(1L, sampleTransaction, LocalDateTime.now(), true, LocalDateTime.now().minusDays(20L));
    }

    @Test
    void getRecurringTransactionById_ShouldReturnRecurringTransaction() {
        when(recurringTransactionRepository.findById(1L)).thenReturn(Optional.of(sampleRecurringTransaction));

        RecurringTransaction result = recurringTransactionService.getRecurringTransactionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertTrue(result.isActive());
    }

    @Test
    void getRecurringTransactionById_WhenNotFound_ShouldThrowException() {
        when(recurringTransactionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recurringTransactionService.getRecurringTransactionById(999L));
    }

    @Test
    void createRecurringTransaction_ShouldSaveAndReturnRecurringTransaction() {
        Category sampleCategory = new Category(1L, Category.categories.CREDIT_CARD, LocalDateTime.now());

        Transaction sampleTransaction = new Transaction
                (1L,
                        sampleCategory,
                        "food",
                        new BigDecimal("1.10"),
                        LocalDateTime.now(),
                        false,
                        null);


        RecurringTransaction savedRecurringTransaction = new RecurringTransaction(1L, sampleTransaction, LocalDateTime.now(), true, LocalDateTime.now().minusDays(20L));
        RecurringTransaction newRecurringTransaction = new RecurringTransaction(null, sampleTransaction, LocalDateTime.now(), true, LocalDateTime.now().minusDays(20L));

        when(recurringTransactionRepository.save(any(RecurringTransaction.class))).thenReturn(savedRecurringTransaction);

        RecurringTransaction created = recurringTransactionService.createRecurringTransaction(newRecurringTransaction);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        verify(recurringTransactionRepository, times(1)).save(any(RecurringTransaction.class));
    }


    @Test
    void createRecurringTransaction_WhenAlreadyExists_ShouldThrowException() {
        Category sampleCategory = new Category(1L, Category.categories.CREDIT_CARD, LocalDateTime.now());
        Transaction sampleTransaction = new Transaction
                (1L, sampleCategory, "food", new BigDecimal("1.10"), LocalDateTime.now(), false, null);
        sampleRecurringTransaction = new RecurringTransaction(1L, sampleTransaction, LocalDateTime.now(), true, LocalDateTime.now().minusDays(20L));

        assertThrows(ResourceNotFoundException.class, () -> recurringTransactionService.createRecurringTransaction(sampleRecurringTransaction));
        verify(recurringTransactionRepository, never()).save(any(RecurringTransaction.class));
    }

    @Test
    void updateRecurringTransaction_ShouldUpdateAndReturnRecurringTransaction() {
        Category sampleCategory = new Category(1L, Category.categories.CREDIT_CARD, LocalDateTime.now());
        Transaction sampleTransaction = new Transaction
                (1L, sampleCategory, "food", new BigDecimal("1.10"), LocalDateTime.now(), false, null);
        sampleRecurringTransaction = new RecurringTransaction(1L, sampleTransaction, LocalDateTime.now(),
                true, LocalDateTime.now().minusDays(20L));


        when(recurringTransactionRepository.existsById(1L)).thenReturn(true);
        when(recurringTransactionRepository.findById(1L)).thenReturn(Optional.of(sampleRecurringTransaction));
        when(recurringTransactionRepository.save(any(RecurringTransaction.class))).thenReturn(sampleRecurringTransaction);

        RecurringTransaction result = recurringTransactionService.updateRecurringTransaction(1L, sampleRecurringTransaction);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertTrue(result.isActive());
        verify(recurringTransactionRepository, times(1)).save(any(RecurringTransaction.class));
    }

    @Test
    void updateRecurringTransaction_WhenNotFound_ShouldThrowException() {
        when(recurringTransactionRepository.existsById(999L)).thenReturn(false);

        Category testCategory = new Category(null, Category.categories.MISC, LocalDateTime.now());
        Transaction testTransaction = new Transaction
                (1L, testCategory, "food",
                        new BigDecimal("2.10"), LocalDateTime.now(), false, null);
        RecurringTransaction updatedRecurringTransaction = new RecurringTransaction(1L, testTransaction, LocalDateTime.now(), true, LocalDateTime.now().minusDays(20L));

        assertThrows(ResourceNotFoundException.class, () -> recurringTransactionService.updateRecurringTransaction(999L, updatedRecurringTransaction));
        verify(recurringTransactionRepository, never()).save(any(RecurringTransaction.class));
    }

    @Test
    void deleteRecurringTransaction_ShouldCallRepositoryDelete() {
        when(recurringTransactionRepository.existsById(1L)).thenReturn(true);

        recurringTransactionService.deleteRecurringTransaction(1L);

        verify(recurringTransactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteRecurringTransaction_WhenNotFound_ShouldThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> recurringTransactionService.deleteRecurringTransaction(999L));
        verify(recurringTransactionRepository, never()).deleteById(anyLong());
    }
}