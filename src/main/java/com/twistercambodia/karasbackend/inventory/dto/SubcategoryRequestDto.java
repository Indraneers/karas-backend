package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Subcategory;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class SubcategoryRequestDto {
    private String id;
    private String name;
    private String categoryId;
    private int productCount;
    private String color;

    public SubcategoryRequestDto() {}

    public SubcategoryRequestDto(Subcategory subcategory) {
        ModelMapper mapper = new ModelMapper();
        this.id = subcategory.getId();
        this.name = subcategory.getName();
        this.categoryId = subcategory.getCategory().getId();
        this.productCount = subcategory.getProductCount();
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String subcategoryId) {
        this.categoryId = subcategoryId;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
