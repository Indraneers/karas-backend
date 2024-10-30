package com.twistercambodia.karasbackend.inventory.service;

import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import com.twistercambodia.karasbackend.inventory.dto.CategoryDto;
import com.twistercambodia.karasbackend.inventory.entity.Category;
import com.twistercambodia.karasbackend.inventory.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    public Category findByIdOrThrowError(String id) throws RuntimeException {
        return this.categoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found with ID=" + id));
    }

    public Category create(CategoryDto categoryDto) {
        Category category = this.convertToCategory(categoryDto);
        return this.categoryRepository.save(category);
    }

    public Category update(String id, CategoryDto categoryDto) throws RuntimeException {
        Category category = findByIdOrThrowError(id);

        category.setName(categoryDto.getName());
        return this.categoryRepository.save(category);
    }

    public Category delete(String id) throws RuntimeException {
        Category category = this.findByIdOrThrowError(id);

        this.categoryRepository.delete(category);
        return category;
    }

    public CategoryDto convertToCategoryDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }

    public List<CategoryDto> convertToCategoryDto(List<Category> categories) {
        return categories
                .stream()
                .map((category) -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    public Category convertToCategory(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }
}
