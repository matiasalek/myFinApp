package com.myfinapp.controller;

import com.myfinapp.exception.ResourceNotFoundException;
import com.myfinapp.model.Category;
import com.myfinapp.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.sql.Timestamp;
import static com.myfinapp.model.Category.categories.MISC;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {
    // Arrange-Act-Assert pattern
    // Arrange: set up test data -> when x is called, return x
    // Act: Performs the actual operation to be tested
    // Assert: Checks if the result is what is expected

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void getAllCategories_ShouldReturnAllCategories() {
        Long categoryId = 1L;
        Category expectedCategory = new Category(categoryId, MISC, Timestamp.valueOf("2024-07-15 14:30:00"));
        when(categoryService.getCategoryById(categoryId)).thenReturn(expectedCategory);

        ResponseEntity<Category> response = categoryController.getCategoryById(categoryId);

        assertThat(response.getBody()).isEqualTo(expectedCategory);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void createCategory_ShouldCreateCategory() {
        Long categoryId = 3L;
        Timestamp ts = Timestamp.valueOf("2024-07-15 14:30:00");
        Category expectedNewCategory = new Category(categoryId, MISC, ts);
        when(categoryService.createCategory(expectedNewCategory)).thenReturn(expectedNewCategory);

        ResponseEntity<Category> response = categoryController.createCategory(expectedNewCategory);

        assertThat(response.getBody()).isEqualTo(expectedNewCategory);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void updateCategory_ShouldUpdateCategory() {
        Long categoryId = 1L;
        Category updatedCategory = new Category(categoryId, MISC, Timestamp.valueOf("2024-07-15 14:30:00"));
        when(categoryService.updateCategory(categoryId, updatedCategory)).thenReturn(updatedCategory);

        ResponseEntity<Category> response = categoryController.updateCategory(categoryId, updatedCategory);

        assertThat(response.getBody()).isEqualTo(updatedCategory);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(categoryService).updateCategory(categoryId, updatedCategory);
    }


    @Test
    void updateCategory_WhenCategoryNotFound_ShouldReturnNotFound() {
        Long categoryId = 999L;
        Category updatedCategory = new Category(categoryId, MISC, Timestamp.valueOf("2024-07-15 14:30:00"));

        when(categoryService.updateCategory(categoryId, updatedCategory)).thenThrow(
                new ResourceNotFoundException("Category not found"));


        ResponseEntity<Category> response = categoryController.updateCategory(categoryId, updatedCategory);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}