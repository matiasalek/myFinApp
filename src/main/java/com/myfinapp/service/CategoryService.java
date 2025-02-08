package com.myfinapp.service;

import com.myfinapp.exception.ResourceNotFoundException;
import com.myfinapp.model.Category;
import com.myfinapp.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    public Category createCategory(Category category){
        if (category.getId() != null) {
            throw new ResourceNotFoundException("Category already exists");
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category categoryDetails){
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found");
        }
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(categoryDetails.getName());
                    category.setDate(categoryDetails.getDate());
                    return categoryRepository.save(category);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    public Category patchCategory(Long id, Map<String, Object> updates) {
        return categoryRepository.findById(id).map(category -> {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "name":
                        category.setName(Category.categories.valueOf((String) value));
                        break;
                    case "date":
                        category.setDate((LocalDateTime) value);
                        break;
                }
            });
            return categoryRepository.save(category);
        }).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    public void deleteCategory(Long id){
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }
}