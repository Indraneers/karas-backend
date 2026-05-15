package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Subcategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SubcategoryRequestDto {
    private String id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Category is required")
    private String categoryId;

    private int productCount;
    private String color;

    public SubcategoryRequestDto() {}

    public SubcategoryRequestDto(Subcategory subcategory) {
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
