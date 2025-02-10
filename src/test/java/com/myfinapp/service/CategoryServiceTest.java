package com.myfinapp.service;

import com.myfinapp.exception.ResourceNotFoundException;
import com.myfinapp.model.Category;
import com.myfinapp.repository.CategoryRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = new Category(1L, Category.categories.CREDIT_CARD, LocalDateTime.now());
    }

    @Test
    void getCategoryById_ShouldReturnCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));

        Category result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Category.categories.CREDIT_CARD, result.getName());
    }

    @Test
    void getCategoryById_WhenNotFound_ShouldThrowException() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(999L));
    }

    @Test
    void createCategory_ShouldSaveAndReturnCategory() {
        Category inputCategory = new Category(null, Category.categories.MISC, LocalDateTime.now());
        Category savedCategory = new Category(1L, Category.categories.MISC, LocalDateTime.now());

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        Category created = categoryService.createCategory(inputCategory);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void createCategory_WhenAlreadyExists_ShouldThrowException() {
        Category existingCategory = new Category(1L, Category.categories.MISC, LocalDateTime.now());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.createCategory(existingCategory));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void updateCategory_ShouldUpdateAndReturnCategory() {
        Category updatedCategory = new Category(1L, Category.categories.MISC, LocalDateTime.now());

        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        Category result = categoryService.updateCategory(1L, updatedCategory);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Category.categories.MISC, result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategory_WhenNotFound_ShouldThrowException() {
        when(categoryRepository.existsById(999L)).thenReturn(false);

        Category updatedCategory = new Category(999L, Category.categories.MISC, LocalDateTime.now());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.updateCategory(999L, updatedCategory));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void deleteCategory_ShouldCallRepositoryDelete() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCategory_WhenNotFound_ShouldThrowException() {
        when(categoryRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategory(999L));
        verify(categoryRepository, never()).deleteById(anyLong());
    }
}