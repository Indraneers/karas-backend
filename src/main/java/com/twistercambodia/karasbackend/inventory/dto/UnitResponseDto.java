package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Unit;
import org.modelmapper.ModelMapper;

import static com.twistercambodia.karasbackend.utils.MappingUtils.map;

public class UnitResponseDto {
    private String id;
    private String name;
    private int quantity;
    private ProductDto product;
    private int price;
    private String sku;
    private int toBaseUnit;

    public UnitResponseDto() {}

    public UnitResponseDto(Unit unit) {
        this.id = unit.getId();
        this.name = unit.getName();
        this.quantity = unit.getQuantity();
        this.product = map(unit.getProduct(), ProductDto.class);
        this.price = unit.getPrice();
        this.sku = unit.getSku();
        this.toBaseUnit = unit.getToBaseUnit();
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getToBaseUnit() {
        return toBaseUnit;
    }

    public void setToBaseUnit(int toBaseUnit) {
        this.toBaseUnit = toBaseUnit;
    }
}
