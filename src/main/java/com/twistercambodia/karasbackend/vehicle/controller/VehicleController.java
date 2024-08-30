package com.twistercambodia.karasbackend.vehicle.controller;

import com.twistercambodia.karasbackend.customer.service.CustomerService;
import com.twistercambodia.karasbackend.vehicle.dto.VehicleDto;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import com.twistercambodia.karasbackend.vehicle.service.VehicleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public VehicleDto createVehicle(@RequestBody VehicleDto vehicleDto) {
        Vehicle vehicle = this.vehicleService.create(vehicleDto);
        return this.vehicleService.convertToVehicleDto(vehicle);
    }
}
