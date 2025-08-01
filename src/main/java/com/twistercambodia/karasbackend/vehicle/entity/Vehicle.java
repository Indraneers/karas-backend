package com.twistercambodia.karasbackend.vehicle.entity;

import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.maintenance.entity.Maintenance;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.Set;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="customer_id", nullable=false)
    private Customer customer;

    @Column()
    private String vinNo;

    @Column()
    private String engineNo;

    @Column()
    private int mileage;

    @Column
    private String note;

    @Column(unique = true)
    private String plateNumber;

    @Column()
    private String makeAndModel;

    @OneToOne(fetch = FetchType.EAGER)
    private Maintenance futureMaintenance;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Maintenance> maintenances;

    @Convert(converter = VehicleTypeConverter.class)
    @Column()
    private VehicleType vehicleType;

    @Column()
    private Instant createdAt;

    @Column()
    private Instant updatedAt;

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

    public Maintenance getFutureMaintenance() {
        return futureMaintenance;
    }

    public void setFutureMaintenance(Maintenance futureMaintenance) {
        this.futureMaintenance = futureMaintenance;
    }

    public Set<Maintenance> getMaintenances() {
        return maintenances;
    }

    public void setMaintenances(Set<Maintenance> maintenances) {
        this.maintenances = maintenances;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id='" + id + '\'' +
                ", vinNo='" + vinNo + '\'' +
                ", engineNo='" + engineNo + '\'' +
                ", mileage=" + mileage +
                ", note='" + note + '\'' +
                ", plateNumber='" + plateNumber + '\'' +
                ", makeAndModel='" + makeAndModel + '\'' +
                ", futureMaintenance=" + futureMaintenance +
                ", maintenances=" + maintenances +
                ", vehicleType=" + vehicleType +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
