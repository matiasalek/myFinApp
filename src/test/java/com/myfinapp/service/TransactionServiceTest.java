package com.myfinapp.service;

import com.myfinapp.exception.ResourceNotFoundException;
import com.myfinapp.model.Category;
import com.myfinapp.model.Transaction;
import com.myfinapp.repository.TransactionRepository;
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
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction sampleTransaction;

    @BeforeEach
    void setUp() {
        Category sampleCategory = new Category(1L, Category.categories.CREDIT_CARD);
        sampleTransaction = new Transaction
                (1L, sampleCategory, "food", new BigDecimal("1.10"), LocalDateTime.now(), false);
    }

    @Test
    void getTransactionById_ShouldReturnTransaction() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(sampleTransaction));

        Transaction result = transactionService.getTransactionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("food", result.getDescription());
    }

    @Test
    void getTransactionById_WhenNotFound_ShouldThrowException() {
        when(transactionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.getTransactionById(999L));
    }

    @Test
    void createTransaction_ShouldSaveAndReturnTransaction() {

        Category testCategory = new Category(1L, Category.categories.CREDIT_CARD);


        Transaction newTransaction = new Transaction
                (null, testCategory, "food", new BigDecimal("1.10"), LocalDateTime.now(), false);


        Transaction savedTransaction = new Transaction
                (1L, testCategory, "food", new BigDecimal("1.10"), LocalDateTime.now(), false);

        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        Transaction created = transactionService.createTransaction(newTransaction);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }


    @Test
    void createTransaction_WhenAlreadyExists_ShouldThrowException() {
        Category testCategory = new Category(1L, Category.categories.CREDIT_CARD);
        Transaction existingTransaction = new Transaction
                (1L, testCategory, "food",
                        new BigDecimal("1.10"), LocalDateTime.now(), false);


        assertThrows(ResourceNotFoundException.class, () -> transactionService.createTransaction(existingTransaction));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void updateTransaction_ShouldUpdateAndReturnTransaction() {
        Category testCategory = new Category(1L, Category.categories.CREDIT_CARD);
        Transaction updatedTransaction = new Transaction
                (1L, testCategory, "food",
                        new BigDecimal("1.10"), LocalDateTime.now(), false);

        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(sampleTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(updatedTransaction);

        Transaction result = transactionService.updateTransaction(1L, updatedTransaction);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("food", result.getDescription());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void updateTransaction_WhenNotFound_ShouldThrowException() {
        when(transactionRepository.findById(999L)).thenReturn(Optional.empty());

        Category testCategory = new Category(null, Category.categories.OTHER);
        Transaction updatedTransaction = new Transaction
                (1L, testCategory, "food",
                        new BigDecimal("2.10"), LocalDateTime.now(), false);

        assertThrows(ResourceNotFoundException.class, () -> transactionService.updateTransaction(999L, updatedTransaction));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void deleteTransaction_ShouldCallRepositoryDelete() {
        when(transactionRepository.existsById(1L)).thenReturn(true);

        transactionService.deleteTransaction(1L);

        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTransaction_WhenNotFound_ShouldThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> transactionService.deleteTransaction(999L));
        verify(transactionRepository, never()).deleteById(anyLong());
    }
}