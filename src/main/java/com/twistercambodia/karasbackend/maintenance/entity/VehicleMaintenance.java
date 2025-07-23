package com.twistercambodia.karasbackend.maintenance.entity;

import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Set;

@Entity
public class VehicleMaintenance {
    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private int nextMileage;

    @Column(nullable = false)
    private Instant nextDate;

    @Column(nullable = false)
    private Instant createdAt;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Vehicle vehicle;

    @OneToMany(fetch =  FetchType.EAGER)
    private Set<MaintenanceAutoService> services;

    @Column
    private String note;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNextMileage() {
        return nextMileage;
    }

    public void setNextMileage(int nextMileage) {
        this.nextMileage = nextMileage;
    }

    public Instant getNextDate() {
        return nextDate;
    }

    public void setNextDate(Instant nextDate) {
        this.nextDate = nextDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Set<MaintenanceAutoService> getServices() {
        return services;
    }

    public void setServices(Set<MaintenanceAutoService> services) {
        this.services = services;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
