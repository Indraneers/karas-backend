package com.twistercambodia.karasbackend.sale.dto;

import com.twistercambodia.karasbackend.sale.entity.Item;

public class ItemRequestDto {
    private String id;
    private int price;
    private int quantity;
    private int discount;
    private String unitId;
    private String serviceId;

    public ItemRequestDto() {}

    public ItemRequestDto(Item item) {
        this.id = item.getId();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();;
        this.discount = item.getDiscount();
        this.unitId = item.getUnit().getId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
