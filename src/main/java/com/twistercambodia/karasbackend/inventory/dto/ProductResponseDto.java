package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Product;
import org.modelmapper.ModelMapper;

public class ProductResponseDto {
    private String id;
    private String name;
    private String identifier;
    private SubcategoryRequestDto subcategory;
    private int unitCount;
    private String baseUnit;
    private boolean variable;
    private String img;

    public ProductResponseDto() {}
    public ProductResponseDto(Product product) {
        ModelMapper mapper = new ModelMapper();
        this.id = product.getId();
        this.name = product.getName();
        this.identifier = product.getIdentifier();
        this.subcategory = mapper.map(product.getSubcategory(), SubcategoryRequestDto.class);
        this.unitCount = product.getUnitCount();
        this.baseUnit = product.getBaseUnit();
        this.variable = product.isVariable();
        this.img = product.getImg();
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public SubcategoryRequestDto getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(SubcategoryRequestDto subcategory) {
        this.subcategory = subcategory;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
