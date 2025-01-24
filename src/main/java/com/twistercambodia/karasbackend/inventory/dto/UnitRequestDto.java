package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Unit;

public class UnitRequestDto {
    private String id;
    private String name;
    private long quantity;
    private String productId;
    private int price;
    private long toBaseUnit;

    public UnitRequestDto() {}

    public UnitRequestDto(Unit unit) {
        this.id = unit.getId();
        this.name = unit.getName();
        this.quantity = unit.getQuantity();
        this.productId = unit.getProduct().getId();
        this.price = unit.getPrice();
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getToBaseUnit() {
        return toBaseUnit;
    }

    public void setToBaseUnit(long toBaseUnit) {
        this.toBaseUnit = toBaseUnit;
    }
}
