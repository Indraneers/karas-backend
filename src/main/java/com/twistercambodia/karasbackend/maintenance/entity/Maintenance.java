package com.twistercambodia.karasbackend.maintenance.entity;

import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.maintenance.dto.MaintenanceDto;
import com.twistercambodia.karasbackend.maintenance.dto.MaintenanceServiceDto;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import jakarta.persistence.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Sale sale;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Vehicle vehicle;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int mileage;

    @Column
    private String note;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<MaintenanceService> services;

    public Maintenance() {}

    public Maintenance(MaintenanceDto maintenanceDto) {
        ModelMapper modelMapper = new ModelMapper();
        this.createdAt = maintenanceDto.getCreatedAt();
        this.services = maintenanceDto.getServices()
                .stream()
                .map((ms) -> modelMapper.map(ms, MaintenanceService.class))
                .collect(Collectors.toSet());
        this.mileage = maintenanceDto.getMileage();
        this.note = maintenanceDto.getNote();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
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

    public Set<MaintenanceService> getServices() {
        return services;
    }

    public void setServices(Set<MaintenanceService> services) {
        this.services = services;
    }

    @Override
    public String toString() {
        return "Maintenance{" +
                "id='" + id + '\'' +
                ", vehicle=" + vehicle +
                ", createdAt=" + createdAt +
                ", mileage=" + mileage +
                ", note='" + note + '\'' +
                ", services=" + services +
                '}';
    }
}
