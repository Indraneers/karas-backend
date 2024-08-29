package com.twistercambodia.karasbackend.vehicle.entity;

import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.maintenance.entity.Maintenance;
import jakarta.persistence.*;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false ,nullable = false)
    private Customer customer;

    @Column
    private String vinNo;

    @Column
    private String engineNo;

    @Column
    private String model;

    @Column
    private int mileage;

    @Column
    private String note;

    @Column
    private String plateNumber;

    @Column
    private String makeAndModel;

    @ManyToOne
    private Maintenance maintenance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getVinNo() {
        return vinNo;
    }

    public void setVinNo(String vinNo) {
        this.vinNo = vinNo;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getMakeAndModel() {
        return makeAndModel;
    }

    public void setMakeAndModel(String makeAndModel) {
        this.makeAndModel = makeAndModel;
    }

    @Override
    public String toString() {
        return String.format(
                "Vehicle[id: '%s'," +
                        "customer: '%s'," +
                        "vinNo: '%s'," +
                        "engineNo: '%s'," +
                        "model: '%s'," +
                        "mileage: %d," +
                        "note: '%s'," +
                        "plateNumber: '%s'," +
                        "makeAndModeL '%s']",
                id, vinNo, model, mileage, note, plateNumber
        );
    }
}
