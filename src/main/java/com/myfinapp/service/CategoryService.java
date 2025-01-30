package com.myfinapp.service;

import com.myfinapp.model.Category;
import com.myfinapp.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


    public Category createCategory(Category category){
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category categoryDetails){
        return categoryRepository.findById(id).map(category -> {
            category.setName(categoryDetails.getName());
            category.setDate(categoryDetails.getDate());
            return categoryRepository.save(category);
        }).orElseThrow(()-> new RuntimeException("Category not found"));
    }

    public void deleteCategory(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }
}