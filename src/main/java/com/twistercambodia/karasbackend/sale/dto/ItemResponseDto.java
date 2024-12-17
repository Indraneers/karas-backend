package com.twistercambodia.karasbackend.sale.dto;

import com.twistercambodia.karasbackend.autoService.dto.AutoServiceDto;
import com.twistercambodia.karasbackend.inventory.dto.UnitDto;
import com.twistercambodia.karasbackend.sale.entity.Item;
import org.modelmapper.ModelMapper;

public class ItemResponseDto {
    private String id;
    private int price;
    private int quantity;
    private int discount;
    private UnitDto unit;
    private AutoServiceDto service;

    public ItemResponseDto() {}

    public ItemResponseDto(Item item) {
        ModelMapper mapper = new ModelMapper();
        this.id = item.getId();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();;
        this.discount = item.getDiscount();

        if (item.getUnit() != null) {
            this.unit = mapper.map(item.getUnit(), UnitDto.class);
        } else {
            this.service = mapper.map(item.getService(), AutoServiceDto.class);
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

    public UnitDto getUnit() {
        return unit;
    }

    public void setUnit(UnitDto unit) {
        this.unit = unit;
    }

    public AutoServiceDto getService() {
        return service;
    }

    public void setService(AutoServiceDto service) {
        this.service = service;
    }
}
