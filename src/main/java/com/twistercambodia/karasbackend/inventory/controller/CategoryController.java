package com.twistercambodia.karasbackend.inventory.controller;

import com.twistercambodia.karasbackend.inventory.dto.CategoryDto;
import com.twistercambodia.karasbackend.inventory.entity.Category;
import com.twistercambodia.karasbackend.inventory.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> getAllCategory(
            @RequestParam(value = "q", required = false) String q
    ) {
        return this.categoryService.convertToCategoryDto(
                this.categoryService.findAll(q)
        );
    }

    @GetMapping("{id}")
    public CategoryDto getCategoryById(
            @PathVariable("id") String id
    ) {
        return this.categoryService.convertToCategoryDto(
                this.categoryService.findByIdOrThrowError(id)
        );
    }

    @PostMapping
    public CategoryDto createCategory(
            @RequestBody CategoryDto categoryDto
    ) {
        Category category = this.categoryService.create(categoryDto);
        this.logger.info("Creating category={}", category);
        return this.categoryService.convertToCategoryDto(category);
    }

    @PutMapping("{id}")
    public CategoryDto updateCategory(
            @RequestBody CategoryDto categoryDto,
            @PathVariable("id") String id
    ) throws RuntimeException {
        Category category = this.categoryService.update(id, categoryDto);
        this.logger.info("Updating category={}", category);
        return this.categoryService.convertToCategoryDto(category);
    }

    @DeleteMapping("{id}")
    public CategoryDto deleteCategory(
            @PathVariable("id") String id
    ) throws RuntimeException {
        Category category = this.categoryService.delete(id);
        this.logger.info("Deleting category={}", category);
        return this.categoryService.convertToCategoryDto(category);
    }
}
