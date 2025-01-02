package com.twistercambodia.karasbackend.sale.dto;

import com.twistercambodia.karasbackend.inventory.dto.UnitRequestDto;
import com.twistercambodia.karasbackend.sale.entity.Item;

import static com.twistercambodia.karasbackend.utils.MappingUtils.map;

public class ItemResponseDto {
    private String id;
    private int price;
    private int quantity;
    private int discount;
    private UnitRequestDto unit;

    public ItemResponseDto() {}

    public ItemResponseDto(Item item) {
        this.id = item.getId();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();;
        this.discount = item.getDiscount();
        this.unit = map(item.getUnit(), UnitRequestDto.class);
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

    public UnitRequestDto getUnit() {
        return unit;
    }

    public void setUnit(UnitRequestDto unit) {
        this.unit = unit;
    }
}
