package com.twistercambodia.karasbackend.inventory.controller;

import com.twistercambodia.karasbackend.inventory.entities.Category;
import com.twistercambodia.karasbackend.inventory.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAllCategory() {
        return this.categoryService.findAll();
    }
}
