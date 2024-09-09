package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Unit;

public class UnitDto {
    private String id;
    private String name;
    private int quantity;
    private String productId;
    private int price;

    public UnitDto() {}

    public UnitDto(Unit unit) {
        this.id = unit.getId();
        this.name = unit.getName();
        this.quantity = unit.getQuantity();
        this.productId = unit.getProduct().getId();
        this.price = unit.getPrice();
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
}
