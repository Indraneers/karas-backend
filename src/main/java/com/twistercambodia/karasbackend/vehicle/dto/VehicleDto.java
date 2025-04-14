package com.twistercambodia.karasbackend.vehicle.dto;

import com.twistercambodia.karasbackend.customer.dto.CustomerDto;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import com.twistercambodia.karasbackend.vehicle.entity.VehicleType;

public class VehicleDto {
    private String id;
    private String customerId;
    private String vinNo;
    private String engineNo;
    private int mileage;
    private String note;
    private String plateNumber;
    private String makeAndModel;
    private CustomerDto customer;
    private VehicleType vehicleType;

    public VehicleDto() {}

    public VehicleDto(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.vinNo = vehicle.getVinNo();
        this.engineNo = vehicle.getEngineNo();
        this.mileage = vehicle.getMileage();
        this.note = vehicle.getNote();
        this.plateNumber = vehicle.getPlateNumber();
        this.makeAndModel = vehicle.getMakeAndModel();
        this.customer = new CustomerDto(vehicle.getCustomer());
        this.vehicleType = vehicle.getVehicleType();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public CustomerDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
