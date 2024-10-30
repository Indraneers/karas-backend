package com.twistercambodia.karasbackend.autoService.dto;

import com.twistercambodia.karasbackend.autoService.entity.AutoService;

public class AutoServiceDto {
    private String id;
    private String name;
    private boolean isActive;
    private int originalPrice;

    public AutoServiceDto() {}

    public AutoServiceDto(AutoService autoService) {
        this.id = autoService.getId();
        this.name = autoService.getName();
        this.isActive = autoService.isActive();
        this.originalPrice = autoService.getOriginalPrice();
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }
}
