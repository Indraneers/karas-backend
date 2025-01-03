package com.twistercambodia.karasbackend.inventory.controller;

import com.twistercambodia.karasbackend.inventory.dto.SubcategoryDto;
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
    public List<SubcategoryDto> getAllCategory(
            @RequestParam(value = "q", required = false) String q
    ) {
        return this.subcategoryService.convertToSubcategoryDto(
                this.subcategoryService.findAll(q)
        );
    }

    @GetMapping("{id}")
    public SubcategoryDto getCategoryById(
            @PathVariable("id") String id
    ) {
        return this.subcategoryService.convertToSubcategoryDto(
                this.subcategoryService.findByIdOrThrowError(id)
        );
    }

    @PostMapping
    public SubcategoryDto createCategory(
            @RequestBody SubcategoryDto subcategoryDto
    ) {
        Subcategory subcategory = this.subcategoryService.create(subcategoryDto);
        this.logger.info("Creating subcategory={}", subcategory);
        return this.subcategoryService.convertToSubcategoryDto(subcategory);
    }

    @PutMapping("{id}")
    public SubcategoryDto updateCategory(
            @RequestBody SubcategoryDto subcategoryDto,
            @PathVariable("id") String id
    ) throws RuntimeException {
        Subcategory subcategory = this.subcategoryService.update(id, subcategoryDto);
        this.logger.info("Updating subcategory={}", subcategory);
        return this.subcategoryService.convertToSubcategoryDto(subcategory);
    }

    @DeleteMapping("{id}")
    public SubcategoryDto deleteCategory(
            @PathVariable("id") String id
    ) throws RuntimeException {
        Subcategory subcategory = this.subcategoryService.delete(id);
        this.logger.info("Deleting subcategory={}", subcategory);
        return this.subcategoryService.convertToSubcategoryDto(subcategory);
    }
}
