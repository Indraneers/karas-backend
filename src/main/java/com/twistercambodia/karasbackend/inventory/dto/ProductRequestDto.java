package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Product;
import org.modelmapper.ModelMapper;

public class ProductRequestDto {
    private String id;
    private String name;
    private String subcategoryId;
    private int unitCount;
    private String baseUnit;
    private boolean variable;

    public ProductRequestDto() {}

    public ProductRequestDto(Product product) {
        ModelMapper mapper = new ModelMapper();
        this.id = product.getId();
        this.name = product.getName();
        this.subcategoryId = product.getSubcategory().getId();
        this.unitCount = product.getUnitCount();
        this.baseUnit = product.getBaseUnit();
        this.variable = product.isVariable();
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

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public int getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(int unitCount) {
        this.unitCount = unitCount;
    }

    public String getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(String baseUnit) {
        this.baseUnit = baseUnit;
    }

    public boolean isVariable() {
        return variable;
    }

    public void setVariable(boolean variable) {
        this.variable = variable;
    }
}
