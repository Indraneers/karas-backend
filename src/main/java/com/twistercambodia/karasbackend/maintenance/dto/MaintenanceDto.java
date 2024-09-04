package com.twistercambodia.karasbackend.maintenance.dto;

import com.twistercambodia.karasbackend.maintenance.entity.Maintenance;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class MaintenanceDto {
    private String id;
    private String vehicleId;
    private LocalDateTime createdAt;
    private int mileage;
    private String note;
    private Set<MaintenanceServiceDto> maintenanceServices;

    public MaintenanceDto() {}

    public MaintenanceDto(Maintenance maintenance) {
        ModelMapper modelMapper = new ModelMapper();
        this.id = maintenance.getId();
        this.vehicleId = maintenance.getVehicle().getId();
        this.createdAt = maintenance.getCreatedAt();
        this.mileage = maintenance.getMileage();
        this.note = maintenance.getNote();
        this.maintenanceServices = maintenance
                .getMaintenanceServices()
                .stream()
                .map((ms) -> modelMapper.map(ms, MaintenanceServiceDto.class))
                .collect(Collectors.toSet());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Set<MaintenanceServiceDto> getMaintenanceServices() {
        return maintenanceServices;
    }

    public void setMaintenanceServices(Set<MaintenanceServiceDto> maintenanceServices) {
        this.maintenanceServices = maintenanceServices;
    }
}
