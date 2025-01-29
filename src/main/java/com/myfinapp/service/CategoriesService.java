package com.myfinapp.service;

import com.myfinapp.model.Categories;
import com.myfinapp.repository.CategoriesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository){
        this.categoriesRepository = categoriesRepository;
    }

    @Transactional(readOnly = true)
    public List<Categories> getAllCategories(){
        return categoriesRepository.findAll();
    }


    public Categories createCategory(Categories categories){
        return categoriesRepository.save(categories);
    }

    public Categories updateCategory(Long id, Categories categoriesDetails){
        return categoriesRepository.findById(id).map(categories -> {
            categories.setName(categoriesDetails.getName());
            categories.setDate(categoriesDetails.getDate());
            return categoriesRepository.save(categories);
        }).orElseThrow(()-> new RuntimeException("Category not found"));
    }

    public void deleteCategory(Long id){
        Categories categories = categoriesRepository.findById(id).orElseThrow(()-> new RuntimeException("Category not found"));
        categoriesRepository.delete(categories);
    }
}