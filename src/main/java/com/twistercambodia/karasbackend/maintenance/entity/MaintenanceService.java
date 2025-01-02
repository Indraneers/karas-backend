package com.twistercambodia.karasbackend.maintenance.entity;

import com.twistercambodia.karasbackend.autoService.entity.AutoService;
import jakarta.persistence.*;

@Entity
public class MaintenanceService {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Maintenance maintenance;

    @ManyToOne(fetch = FetchType.EAGER)
    private AutoService autoService;

    @Column(nullable = false)
    private int price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }

    public AutoService getService() {
        return autoService;
    }

    public void setService(AutoService autoService) {
        this.autoService = autoService;
    }

    public AutoService getAutoService() {
        return autoService;
    }

    public void setAutoService(AutoService autoService) {
        this.autoService = autoService;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "MaintenanceService{" +
                "id='" + id + '\'' +
                ", maintenance=" + maintenance +
                ", autoService=" + autoService +
                ", price=" + price +
                '}';
    }
}
