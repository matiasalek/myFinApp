package com.myfinapp.service;

import com.myfinapp.exception.ResourceNotFoundException;
import com.myfinapp.model.Category;
import com.myfinapp.model.Transaction;
import com.myfinapp.repository.CategoryRepository;
import com.myfinapp.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction sampleTransaction;

    @BeforeEach
    void setUp() {
        Category sampleCategory = new Category(1L, Category.categories.CREDIT_CARD);
        sampleTransaction = new Transaction
                (1L, sampleCategory, "food", new BigDecimal("1.10"), LocalDate.now(), false);
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
        Transaction newTransaction = new Transaction(
                null, testCategory, "food", new BigDecimal("1.10"), LocalDate.now(), false);
        Transaction savedTransaction = new Transaction(
                1L, testCategory, "food", new BigDecimal("1.10"), LocalDate.now(), false);

        when(categoryRepository.findByName(testCategory.getName())).thenReturn(Optional.of(testCategory));

        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        Transaction created = transactionService.createTransaction(newTransaction);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        verify(categoryRepository, times(1)).findByName(testCategory.getName());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void createTransaction_WhenCategoryNotFound_ShouldThrowException() {
        Category testCategory = new Category(null, Category.categories.CREDIT_CARD);
        Transaction newTransaction = new Transaction(
                null, testCategory, "food", new BigDecimal("1.10"), LocalDate.now(), false);

        when(categoryRepository.findByName(testCategory.getName())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.createTransaction(newTransaction);
        });

        assertEquals("Category not found: CREDIT_CARD", exception.getMessage());

        verify(categoryRepository, times(1)).findByName(testCategory.getName());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void createTransaction_WhenTransactionAlreadyHasId_ShouldThrowException() {
        Category testCategory = new Category(1L, Category.categories.CREDIT_CARD);
        Transaction existingTransaction = new Transaction(
                1L, testCategory, "food", new BigDecimal("1.10"), LocalDate.now(), false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.createTransaction(existingTransaction);
        });

        assertEquals("Transaction already exists", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(categoryRepository, never()).findByName(any());
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