package com.twistercambodia.karasbackend.maintenance.entity;

import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Vehicle vehicle;

    @Column
    private LocalDateTime createdAt;

    @Column
    private int mileage;

    @Column
    private String note;

    @OneToMany(fetch = FetchType.EAGER)
    private List<MaintenanceService> maintenanceService;

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

    public List<MaintenanceService> getMaintenanceService() {
        return maintenanceService;
    }

    public void setMaintenanceService(List<MaintenanceService> maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @Override
    public String toString() {
        return "Maintenance{" +
                "id='" + id + '\'' +
                ", vehicle=" + vehicle +
                ", createdAt=" + createdAt +
                ", mileage=" + mileage +
                ", note='" + note + '\'' +
                ", maintenanceService=" + maintenanceService +
                '}';
    }
}
