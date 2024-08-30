package com.twistercambodia.karasbackend.customer.dto;

import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;

import java.util.Set;

public class CustomerDto {
    private String id;
    private String name;
    private String note;
    private Set<Vehicle> vehicles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
