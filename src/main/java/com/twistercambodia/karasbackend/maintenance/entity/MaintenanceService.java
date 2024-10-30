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

    @Override
    public String toString() {
        return "MaintenanceService{" +
                "id='" + id + '\'' +
                ", maintenance=" + maintenance +
                ", service=" + autoService +
                '}';
    }
}
