package com.twistercambodia.karasbackend.inventory.service;

import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import com.twistercambodia.karasbackend.inventory.dto.SubcategoryDto;
import com.twistercambodia.karasbackend.inventory.entity.Subcategory;
import com.twistercambodia.karasbackend.inventory.repository.SubcategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubcategoryService {
    private final SubcategoryRepository subcategoryRepository;
    private final ModelMapper modelMapper;

    public SubcategoryService(SubcategoryRepository subcategoryRepository, ModelMapper modelMapper) {
        this.subcategoryRepository = subcategoryRepository;
        this.modelMapper = modelMapper;
    }

    public List<Subcategory> findAll(String query) {
        return this.subcategoryRepository.findAll(query);
    }

    public Subcategory findByIdOrThrowError(String id) throws RuntimeException {
        return this.subcategoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found with ID=" + id));
    }

    public Subcategory create(SubcategoryDto subcategoryDto) {
        Subcategory subcategory = this.convertToSubcategory(subcategoryDto);
        return this.subcategoryRepository.save(subcategory);
    }

    public Subcategory update(String id, SubcategoryDto subcategoryDto) throws RuntimeException {
        Subcategory subcategory = findByIdOrThrowError(id);

        subcategory.setName(subcategoryDto.getName());
        return this.subcategoryRepository.save(subcategory);
    }

    public Subcategory delete(String id) throws RuntimeException {
        Subcategory subcategory = this.findByIdOrThrowError(id);

        this.subcategoryRepository.delete(subcategory);
        return subcategory;
    }

    public SubcategoryDto convertToSubcategoryDto(Subcategory subcategory) {
        return modelMapper.map(subcategory, SubcategoryDto.class);
    }

    public List<SubcategoryDto> convertToSubcategoryDto(List<Subcategory> categories) {
        return categories
                .stream()
                .map((category) -> modelMapper.map(category, SubcategoryDto.class))
                .collect(Collectors.toList());
    }

    public Subcategory convertToSubcategory(SubcategoryDto subcategoryDto) {
        return modelMapper.map(subcategoryDto, Subcategory.class);
    }
}
