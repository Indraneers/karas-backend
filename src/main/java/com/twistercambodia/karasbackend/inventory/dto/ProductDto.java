package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entities.Product;

import java.util.List;

public class ProductDto {
    private String id;
    private String name;
    private List<String> unitIds;
    private String categoryId;
    private int unitTotal;

    public ProductDto() {}

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
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

    public List<String> getUnitIds() {
        return unitIds;
    }

    public void setUnitIds(List<String> unitIds) {
        this.unitIds = unitIds;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getUnitTotal() {
        return unitTotal;
    }

    public void setUnitTotal(int unitTotal) {
        this.unitTotal = unitTotal;
    }
}
