package com.myfinapp.controller;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myfinapp.exception.GlobalExceptionHandler;
import com.myfinapp.exception.ResourceNotFoundException;
import com.myfinapp.model.Category;
import com.myfinapp.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
public class CategoryControllerTest {
    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllCategories_ShouldReturnAllCategories() throws Exception {
        Long categoryId = 1L;
        Category expectedCategory = new Category(categoryId, MISC, LocalDateTime.now());
        when(categoryService.getCategoryById(categoryId)).thenReturn(expectedCategory);

        mockMvc.perform(get("/api/category/" + categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value(MISC.toString()))
                .andExpect(jsonPath("$.date").exists());
    }

    @Test
    void createCategory_ShouldCreateCategory() throws Exception {
        Category inputCategory = new Category(null, MISC, LocalDateTime.now());
        Category expectedNewCategory = new Category(3L, MISC, LocalDateTime.now());

        when(categoryService.createCategory(any(Category.class))).thenReturn(expectedNewCategory);

        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inputCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value(MISC.toString()))
                .andExpect(jsonPath("$.date").exists());
    }

    @Test
    void updateCategory_ShouldUpdateCategory() throws Exception {
        Long categoryId = 1L;
        Category updatedCategory = new Category(categoryId, MISC, LocalDateTime.now());
        when(categoryService.updateCategory(eq(categoryId), any(Category.class))).thenReturn(updatedCategory);

        mockMvc.perform(put("/api/category/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value(MISC.toString()))
                .andExpect(jsonPath("$.date").exists());
    }

    @Test
    void updateCategory_WhenCategoryNotFound_ShouldReturnNotFound() throws Exception {
        Long categoryId = 999L;
        Category updatedCategory = new Category(categoryId, MISC,LocalDateTime.now());

        when(categoryService.updateCategory(eq(categoryId), any(Category.class)))
                .thenThrow(new ResourceNotFoundException("Category not found"));

        mockMvc.perform(put("/api/category/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedCategory)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Category not found"));
    }

    @Test
    void deleteCategory_ShouldDeleteCategory() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(delete("/api/category/" + categoryId))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(categoryId);
    }

    @Test
    void deleteCategory_WhenCategoryNotFound_ShouldReturnNotFound() throws Exception {
        Long categoryId = 999L;

        doThrow(new ResourceNotFoundException("Category not found"))
                .when(categoryService).deleteCategory(categoryId);

        mockMvc.perform(delete("/api/category/" + categoryId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Category not found"));
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