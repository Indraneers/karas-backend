package com.twistercambodia.karasbackend.inventory.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twistercambodia.karasbackend.audit.dto.AuditDTO;
import com.twistercambodia.karasbackend.audit.service.AuditService;
import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import com.twistercambodia.karasbackend.inventory.dto.CategoryDto;
import com.twistercambodia.karasbackend.inventory.entity.Category;
import com.twistercambodia.karasbackend.inventory.repository.CategoryRepository;
import com.twistercambodia.karasbackend.storage.service.StorageService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final StorageService storageService;

    public CategoryService(
            CategoryRepository categoryRepository,
            ModelMapper modelMapper,
            StorageService storageService
    ) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.storageService = storageService;
    }

    public List<Category> findAll(String query) {
        return this.categoryRepository.findAll(query);
    }

    public Category findByIdOrThrowError(String id) throws RuntimeException {
        return this.categoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found with ID=" + id));
    }

    @Transactional
    public Category create(CategoryDto categoryDto, MultipartFile image) throws RuntimeException, IOException {
        Category category = this.convertToCategory(categoryDto);
        category = this.categoryRepository.save(category);
        if (image != null) {
            String ext = storageService.getExtension(image.getOriginalFilename());
            if (ext == "svg") {
                category.setImg(uploadCategoryIcon(category.getId(), image.getInputStream()));
                category = this.categoryRepository.save(category);
            }
        }
        return category;
    }

    @Transactional
    public Category update(String id, CategoryDto categoryDto, MultipartFile image) throws RuntimeException, IOException {
        Category category = findByIdOrThrowError(id);

        if (image != null) {
            String ext = storageService.getExtension(image.getOriginalFilename());
            if (ext == "svg") {
                category.setImg(uploadCategoryIcon(category.getId(), image.getInputStream()));
            }
        }

        category.setName(categoryDto.getName());
        category.setColor(categoryDto.getColor());

        return this.categoryRepository.save(category);
    }

    @Transactional
    public Category delete(String id) throws RuntimeException {
        Category category = this.findByIdOrThrowError(id);
        if (category.getImg() != null && !category.getImg().isEmpty()) {
            deleteCategoryIcon(category.getImg());
        }
        this.categoryRepository.delete(category);
        return category;
    }


    public CategoryDto convertToCategoryDto(Category category) {
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        if (!category.getImg().isEmpty()) {
            categoryDto.setImg(
                    storageService.generatePresignedUrl(
                            category.getImg(),
                            Duration.ofHours(1)
                    )
            );
        }

        return categoryDto;
    }

    public List<CategoryDto> convertToCategoryDto(List<Category> categories) {
        return categories
                .stream()
                .map(this::convertToCategoryDto)
                .collect(Collectors.toList());
    }

    public Category convertToCategory(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }

    public String getCategoryIcon(String id) {

        return "/categories/" + id + ".svg";
    }

    public String uploadCategoryIcon(String id, InputStream inputStream) {
        String filename = getCategoryIcon(id);
        storageService.uploadFile(
                filename,
                inputStream
        );
        return filename;
    }

    public void deleteCategoryIcon(String filename) {
        storageService.deleteFile(
                filename
        );
    }
}
