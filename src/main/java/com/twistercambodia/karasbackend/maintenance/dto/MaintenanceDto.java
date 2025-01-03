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
    private LocalDateTime createdAt;
    private LocalDateTime nextMaintenanceDate;
    private int mileage;
    private String note;
    private Set<MaintenanceServiceDto> services;

    public MaintenanceDto() {}

    public MaintenanceDto(Maintenance maintenance) {
        ModelMapper mapper = new ModelMapper();
        this.id = maintenance.getId();
        this.saleId = maintenance.getSale().getId();
        this.vehicleId = maintenance.getVehicle().getId();
        this.createdAt = maintenance.getCreatedAt();
        this.mileage = maintenance.getMileage();
        this.note = maintenance.getNote();
        this.services = maintenance
                .getServices()
                .stream()
                .map((ms) -> mapper.map(ms, MaintenanceServiceDto.class))
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(LocalDateTime nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
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

    public Set<MaintenanceServiceDto> getServices() {
        return services;
    }

    public void setServices(Set<MaintenanceServiceDto> services) {
        this.services = services;
    }
}
