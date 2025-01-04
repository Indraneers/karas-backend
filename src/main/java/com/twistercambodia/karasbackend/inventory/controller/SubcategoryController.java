package com.twistercambodia.karasbackend.inventory.controller;

import com.twistercambodia.karasbackend.inventory.dto.SubcategoryRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.SubcategoryResponseDto;
import com.twistercambodia.karasbackend.inventory.entity.Subcategory;
import com.twistercambodia.karasbackend.inventory.service.SubcategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("subcategories")
public class SubcategoryController {
    private final SubcategoryService subcategoryService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SubcategoryController(SubcategoryService subcategoryService) {
        this.subcategoryService = subcategoryService;
    }

    @GetMapping
    public List<SubcategoryResponseDto> getAllSubcategory(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "categoryId", required = false) String categoryId
    ) {
        return this.subcategoryService.convertToSubcategoryDto(
                this.subcategoryService.findAll(q, categoryId)
        );
    }

    @GetMapping("{id}")
    public SubcategoryResponseDto getSubcategoryById(
            @PathVariable("id") String id
    ) {
        return this.subcategoryService.convertToSubcategoryDto(
                this.subcategoryService.findByIdOrThrowError(id)
        );
    }

    @PostMapping
    public SubcategoryResponseDto createSubcategory(
            @RequestBody SubcategoryRequestDto subcategoryRequestDto
    ) {
        Subcategory subcategory = this.subcategoryService.create(subcategoryRequestDto);
        this.logger.info("Creating subcategory={}", subcategory);
        return this.subcategoryService.convertToSubcategoryDto(subcategory);
    }

    @PutMapping("{id}")
    public SubcategoryResponseDto updateSubcategory(
            @RequestBody SubcategoryRequestDto subcategoryRequestDto,
            @PathVariable("id") String id
    ) throws RuntimeException {
        Subcategory subcategory = this.subcategoryService.update(id, subcategoryRequestDto);
        this.logger.info("Updating subcategory={}", subcategory);
        return this.subcategoryService.convertToSubcategoryDto(subcategory);
    }

    @DeleteMapping("{id}")
    public SubcategoryResponseDto deleteSubcategory(
            @PathVariable("id") String id
    ) throws RuntimeException {
        Subcategory subcategory = this.subcategoryService.delete(id);
        this.logger.info("Deleting subcategory={}", subcategory);
        return this.subcategoryService.convertToSubcategoryDto(subcategory);
    }
}
