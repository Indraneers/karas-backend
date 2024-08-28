package com.twistercambodia.karasbackend.maintenance.entity;

import jakarta.persistence.*;

@Entity
public class MaintenanceService {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Maintenance maintenance;

    @ManyToOne(fetch = FetchType.LAZY)
    private Service service;

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

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "MaintenanceService{" +
                "id='" + id + '\'' +
                ", maintenance=" + maintenance +
                ", service=" + service +
                '}';
    }
}
