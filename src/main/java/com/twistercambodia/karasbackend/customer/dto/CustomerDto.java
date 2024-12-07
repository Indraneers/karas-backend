package com.twistercambodia.karasbackend.customer.dto;

import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.vehicle.dto.VehicleDto;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;

public class CustomerDto {
    private String id;
    private String name;
    private String note;
//    private Set<VehicleDto> vehicles;

    public CustomerDto() {}

    public CustomerDto(Customer customer) {
        ModelMapper modelMapper = new ModelMapper();
        this.name = customer.getId();
        this.name = customer.getName();
        this.note = customer.getNote();
//        this.vehicles = customer
//                .getVehicles()
//                .stream()
//                .map((v) -> modelMapper.map(v, VehicleDto.class))
//                .collect(Collectors.toSet());
    }

    public CustomerDto(String name, String note) {
        this.name = name;
        this.note = note;
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
//
//    public Set<VehicleDto> getVehicles() {
//        return vehicles;
//    }
//
//    public void setVehicles(Set<VehicleDto> vehicles) {
//        this.vehicles = vehicles;
//    }
}
