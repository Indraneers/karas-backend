package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Unit;
import org.modelmapper.ModelMapper;

public class UnitResponseDto {
    private String id;
    private String name;
    private long quantity;
    private ProductDto product;
    private int price;
    private String sku;
    private long toBaseUnit;

    public UnitResponseDto() {}

    public UnitResponseDto(Unit unit) {
        ModelMapper mapper = new ModelMapper();
        this.id = unit.getId();
        this.name = unit.getName();
        this.quantity = unit.getQuantity();
        this.product = mapper.map(unit.getProduct(), ProductDto.class);
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

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
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

    public long getToBaseUnit() {
        return toBaseUnit;
    }

    public void setToBaseUnit(long toBaseUnit) {
        this.toBaseUnit = toBaseUnit;
    }
}
