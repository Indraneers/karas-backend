package com.twistercambodia.karasbackend.maintenance.dto;

import com.twistercambodia.karasbackend.autoService.dto.AutoServiceDto;
import com.twistercambodia.karasbackend.maintenance.entity.MaintenanceAutoService;

public class MaintenanceAutoServiceDto {
    private String id;
    private AutoServiceDto service;
    private int price;
    private int discount;

    public MaintenanceAutoServiceDto() {}

    public MaintenanceAutoServiceDto(MaintenanceAutoService maintenanceAutoService) {
        this.id = maintenanceAutoService.getId();
        this.service = new AutoServiceDto(maintenanceAutoService.getService());
        this.price = maintenanceAutoService.getPrice();
        this.discount = maintenanceAutoService.getDiscount();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AutoServiceDto getService() {
        return service;
    }

    public void setService(AutoServiceDto service) {
        this.service = service;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
