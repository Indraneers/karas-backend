package com.twistercambodia.karasbackend.sale.dto;

import com.twistercambodia.karasbackend.inventory.dto.UnitResponseDto;
import com.twistercambodia.karasbackend.sale.entity.Item;
import org.modelmapper.ModelMapper;

public class ItemResponseDto {
    private String id;
    private int price;
    private int quantity;
    private int discount;
    private UnitResponseDto unit;

    public ItemResponseDto() {}

    public ItemResponseDto(Item item) {
        this.id = item.getId();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();;
        this.discount = item.getDiscount();
        this.unit = new UnitResponseDto(item.getUnit());
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

    public UnitResponseDto getUnit() {
        return unit;
    }

    public void setUnit(UnitResponseDto unit) {
        this.unit = unit;
    }
}
