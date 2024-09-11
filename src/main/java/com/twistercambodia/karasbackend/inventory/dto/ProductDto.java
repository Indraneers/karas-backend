package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Product;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDto {
    private String id;
    private String name;
    private List<UnitDto> units;
    private String categoryId;
    private int unitCount;

    public ProductDto() {}

    public ProductDto(Product product) {
        ModelMapper modelMapper = new ModelMapper();
        this.id = product.getId();
        this.name = product.getName();
        this.units =
                product.getUnits().stream()
                        .map((u) -> modelMapper.map(u, UnitDto.class))
                        .collect(Collectors.toList());
        this.categoryId = product.getCategory().getId();
        this.unitCount = product.getUnitCount();
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

    public List<UnitDto> getUnits() {
        return units;
    }

    public void setUnits(List<UnitDto> units) {
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
}
