package com.twistercambodia.karasbackend.customer.entity;

import com.twistercambodia.karasbackend.sale.entity.Sale;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = true)
    private String name;

    @Column
    private String note;

    @OneToMany
    @JoinColumn(name = "customer_id")
    private Set<Vehicle> vehicles;

    @OneToMany
    @JoinColumn(name="customer_id")
    private List<Sale> sales;

    public Customer(List<Sale> sales) {
        this.sales = sales;
    }

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

    @Override
    public String toString() {
        return String.format(
                "Customer[id: '%s'," +
                        "name: '%s'," +
                        "note: '%s'," +
                        "vehicles: [%s]]",
                id, name, note, vehicles
        );
    }
}
