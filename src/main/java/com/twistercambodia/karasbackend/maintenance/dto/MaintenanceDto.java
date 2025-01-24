package com.twistercambodia.karasbackend.maintenance.dto;

import com.twistercambodia.karasbackend.maintenance.entity.Maintenance;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class MaintenanceDto {
    private String id;
    private Long saleId;
    private String vehicleId;
    private String createdAt;
    private int mileage;
    private String note;
    private Set<MaintenanceAutoServiceDto> services;

    public MaintenanceDto() {}

    public MaintenanceDto(Maintenance maintenance) {
        ModelMapper mapper = new ModelMapper();
        this.id = maintenance.getId();
        this.saleId = maintenance.getSale().getId();
        this.vehicleId = maintenance.getVehicle().getId();
        this.createdAt = maintenance.getCreatedAt().toString();
        this.mileage = maintenance.getMileage();
        this.note = maintenance.getNote();
        this.services = maintenance
                .getServices()
                .stream()
                .map((ms) -> mapper.map(ms, MaintenanceAutoServiceDto.class))
                .collect(Collectors.toSet());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<MaintenanceAutoServiceDto> getServices() {
        return services;
    }

    public void setServices(Set<MaintenanceAutoServiceDto> services) {
        this.services = services;
    }
}
