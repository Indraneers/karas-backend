package com.twistercambodia.karasbackend.customer.dto;

import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;

import java.util.Set;

public class CustomerDto {
    private String name;
    private String note;
    private Set<Vehicle> vehicles;

    public CustomerDto(String name, String note) {
        this.name = name;
        this.note = note;
    }

    public CustomerDto(String name, String note, Set<Vehicle> vehicles) {
        this.name = name;
        this.note = note;
        this.vehicles = vehicles;
    }

    public CustomerDto(Customer customer) {
        this.name = customer.getName();
        this.note = customer.getNote();
        this.vehicles = customer.getVehicles();
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
