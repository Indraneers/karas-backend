package com.twistercambodia.karasbackend.maintenance.entity;

import com.twistercambodia.karasbackend.maintenance.dto.MaintenanceDto;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Vehicle vehicle;

    @Column
    private LocalDateTime createdAt;

    @Column
    private int mileage;

    @Column
    private String note;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<MaintenanceService> maintenanceServices;

    public Maintenance() {}

    public Maintenance(MaintenanceDto maintenanceDto) {
        this.createdAt = maintenanceDto.getCreatedAt();
        this.mileage = maintenanceDto.getMileage();
        this.note = maintenanceDto.getNote();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
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

    public Set<MaintenanceService> getMaintenanceServices() {
        return maintenanceServices;
    }

    public void setMaintenanceServices(Set<MaintenanceService> maintenanceServices) {
        this.maintenanceServices = maintenanceServices;
    }

    @Override
    public String toString() {
        return "Maintenance{" +
                "id='" + id + '\'' +
                ", vehicle=" + vehicle +
                ", createdAt=" + createdAt +
                ", mileage=" + mileage +
                ", note='" + note + '\'' +
                ", maintenanceService=" + maintenanceServices +
                '}';
    }
}
