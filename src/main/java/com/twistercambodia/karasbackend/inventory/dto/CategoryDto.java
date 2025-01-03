package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Category;
import org.modelmapper.ModelMapper;

import java.util.List;

public class CategoryDto {
    private String id;
    private String name;
    private List<SubcategoryDto> subcategories;
    private int subcategoryCount;

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
        this.subcategoryCount = category.getSubcategoryCount();
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

    public int getSubcategoryCount() {
        return subcategoryCount;
    }

    public void setSubcategoryCount(int subcategoryCount) {
        this.subcategoryCount = subcategoryCount;
    }
}
