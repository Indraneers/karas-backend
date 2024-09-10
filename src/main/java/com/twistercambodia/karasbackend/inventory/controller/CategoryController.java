package com.twistercambodia.karasbackend.inventory.controller;

import com.twistercambodia.karasbackend.exception.dto.ErrorResponse;
import com.twistercambodia.karasbackend.inventory.dto.CategoryDto;
import com.twistercambodia.karasbackend.inventory.entity.Category;
import com.twistercambodia.karasbackend.inventory.exception.CategoryNotFoundException;
import com.twistercambodia.karasbackend.inventory.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
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
    public List<CategoryDto> getAllCategory() {
        return this.categoryService.convertToCategoryDto(
                this.categoryService.findAll()
        );
    }

    @GetMapping("{id}")
    public CategoryDto getAllCategory(
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

    @ExceptionHandler(value = CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCategoryNotFound(CategoryNotFoundException exception) {
        this.logger.error("Throwing CategoryNotFoundException with message={}", exception.getMessage());
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Category not found"
        );
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        this.logger.error("Throwing DataIntegrityViolationException with message={}", exception.getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Category with the same name already exist"
        );
    }
}
