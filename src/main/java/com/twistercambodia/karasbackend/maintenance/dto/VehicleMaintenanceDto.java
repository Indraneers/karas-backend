package com.twistercambodia.karasbackend.maintenance.dto;

import com.twistercambodia.karasbackend.maintenance.entity.VehicleMaintenance;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class VehicleMaintenanceDto {
    private String id;
    private int nextMilage;
    private LocalDateTime nextDate;
    private LocalDateTime createdAt;
    private String vehicleId;
    private Set<MaintenanceAutoServiceDto> services;
    private String note;

    public VehicleMaintenanceDto() {

    }

    public VehicleMaintenanceDto(VehicleMaintenance vehicleMaintenance) {
        this.id = vehicleMaintenance.getId();
        this.nextMilage = vehicleMaintenance.getNextMileage();
        this.nextDate = vehicleMaintenance.getNextDate();
        this.createdAt = vehicleMaintenance.getCreatedAt();
        this.vehicleId = vehicleMaintenance.getVehicle().getId();
        this.services = vehicleMaintenance
                .getServices()
                .stream()
                .map(MaintenanceAutoServiceDto::new)
                .collect(Collectors.toSet());
        this.note = vehicleMaintenance.getNote();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNextMilage() {
        return nextMilage;
    }

    public void setNextMilage(int nextMilage) {
        this.nextMilage = nextMilage;
    }

    public LocalDateTime getNextDate() {
        return nextDate;
    }

    public void setNextDate(LocalDateTime nextDate) {
        this.nextDate = nextDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Set<MaintenanceAutoServiceDto> getServices() {
        return services;
    }

    public void setServices(Set<MaintenanceAutoServiceDto> services) {
        this.services = services;
    }


}
