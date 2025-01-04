package com.twistercambodia.karasbackend.inventory.service;

import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import com.twistercambodia.karasbackend.inventory.dto.SubcategoryRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.SubcategoryResponseDto;
import com.twistercambodia.karasbackend.inventory.entity.Category;
import com.twistercambodia.karasbackend.inventory.entity.Subcategory;
import com.twistercambodia.karasbackend.inventory.repository.SubcategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubcategoryService {
    private final SubcategoryRepository subcategoryRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public SubcategoryService
            (SubcategoryRepository subcategoryRepository, CategoryService categoryService, ModelMapper modelMapper) {
        this.subcategoryRepository = subcategoryRepository;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    public List<Subcategory> findAll(String query, String categoryId) {
        return this.subcategoryRepository.findAll(query, categoryId);
    }

    public Subcategory findByIdOrThrowError(String id) throws RuntimeException {
        return this.subcategoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Subcategory Not Found with ID=" + id));
    }

    public Subcategory create(SubcategoryRequestDto subcategoryRequestDto) {
        Subcategory subcategory = this.convertToSubcategory(subcategoryRequestDto);
        return this.subcategoryRepository.save(subcategory);
    }

    public Subcategory update(String id, SubcategoryRequestDto subcategoryRequestDto) throws RuntimeException {
        Subcategory subcategory = findByIdOrThrowError(id);
        Category category = categoryService.findByIdOrThrowError(subcategoryRequestDto.getCategoryId());

        subcategory.setName(subcategoryRequestDto.getName());
        subcategory.setCategory(category);
        return this.subcategoryRepository.save(subcategory);
    }

    public Subcategory delete(String id) throws RuntimeException {
        Subcategory subcategory = this.findByIdOrThrowError(id);

        this.subcategoryRepository.delete(subcategory);
        return subcategory;
    }

    public SubcategoryResponseDto convertToSubcategoryDto(Subcategory subcategory) {
        return modelMapper.map(subcategory, SubcategoryResponseDto.class);
    }

    public List<SubcategoryResponseDto> convertToSubcategoryDto(List<Subcategory> categories) {
        return categories
                .stream()
                .map((category) -> modelMapper.map(category, SubcategoryResponseDto.class))
                .collect(Collectors.toList());
    }

    public Subcategory convertToSubcategory(SubcategoryRequestDto subcategoryRequestDto) {
        return modelMapper.map(subcategoryRequestDto, Subcategory.class);
    }
}
