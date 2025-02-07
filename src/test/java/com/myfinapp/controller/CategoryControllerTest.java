package com.myfinapp.controller;

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
import static org.assertj.core.api.Assertions.as;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {
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
}