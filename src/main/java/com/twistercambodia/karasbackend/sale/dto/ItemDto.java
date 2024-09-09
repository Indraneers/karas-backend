package com.twistercambodia.karasbackend.sale.dto;

import com.twistercambodia.karasbackend.sale.entity.Item;

public class ItemDto {
    private String id;
    private int price;
    private int quantity;
    private int discount;
    private String productId;
    private String serviceId;

    public ItemDto() {}

    public ItemDto(Item item) {
        this.id = item.getId();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();;
        this.discount = item.getDiscount();

        if (item.getProduct() != null) {
            this.productId = item.getProduct().getId();
        } else {
            this.serviceId = item.getService().getId();
        }
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
