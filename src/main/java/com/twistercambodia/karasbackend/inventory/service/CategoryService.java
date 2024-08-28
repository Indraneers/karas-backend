package com.twistercambodia.karasbackend.inventory.service;

import com.twistercambodia.karasbackend.inventory.entities.Category;
import com.twistercambodia.karasbackend.inventory.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }
}
