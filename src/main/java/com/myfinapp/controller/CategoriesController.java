package com.myfinapp.controller;


import com.myfinapp.model.Categories;
import com.myfinapp.service.CategoriesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {
    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService){
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public List<Categories> getAllCategories(){
        return categoriesService.getAllCategories();
    }

    @PostMapping
    public ResponseEntity<Categories> createCategory(@RequestBody Categories categories){
        Categories createdCategories = categoriesService.createCategory(categories);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategories);
    }
}
