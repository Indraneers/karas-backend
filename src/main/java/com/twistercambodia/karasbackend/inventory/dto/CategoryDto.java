package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Category;
import org.modelmapper.ModelMapper;

import java.util.List;

public class CategoryDto {
    private String id;
    private String name;
    private List<SubcategoryDto> subcategories;
    private int subcategoriesCount;

    public CategoryDto() {}

    public CategoryDto(Category category) {
        ModelMapper mapper = new ModelMapper();
        this.id = category.getId();;
        this.name = category.getName();
        this.subcategories = category
                .getSubcategories()
                .stream()
                .map(sc -> mapper.map(sc, SubcategoryDto.class))
                .toList();
        this.subcategoriesCount = category.getSubcategoriesCount();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubcategoryDto> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<SubcategoryDto> subcategories) {
        this.subcategories = subcategories;
    }

    public int getSubcategoriesCount() {
        return subcategoriesCount;
    }

    public void setSubcategoriesCount(int subcategoriesCount) {
        this.subcategoriesCount = subcategoriesCount;
    }
}
