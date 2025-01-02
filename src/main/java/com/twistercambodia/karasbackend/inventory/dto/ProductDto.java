package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Product;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

import static com.twistercambodia.karasbackend.utils.MappingUtils.map;

public class ProductDto {
    private String id;
    private String name;
    private List<UnitRequestDto> units;
    private String categoryId;
    private int unitCount;
    private String baseUnit;
    private boolean variable;

    public ProductDto() {}

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.units =
                product.getUnits().stream()
                        .map(u -> map(u, UnitRequestDto.class))
                        .collect(Collectors.toList());
        this.categoryId = product.getCategory().getId();
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

    public List<UnitRequestDto> getUnits() {
        return units;
    }

    public void setUnits(List<UnitRequestDto> units) {
        this.units = units;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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
