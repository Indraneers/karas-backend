package com.twistercambodia.karasbackend.vehicle.controller;

import com.twistercambodia.karasbackend.customer.service.CustomerService;
import com.twistercambodia.karasbackend.exception.dto.ErrorResponse;
import com.twistercambodia.karasbackend.vehicle.dto.VehicleDto;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import com.twistercambodia.karasbackend.vehicle.exception.VehicleNotFoundException;
import com.twistercambodia.karasbackend.vehicle.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public VehicleDto createVehicle(@RequestBody VehicleDto vehicleDto) {
        Vehicle vehicle = this.vehicleService.create(vehicleDto);
        this.logger.info("Creating vehicle={}", vehicle);
        return this.vehicleService.convertToVehicleDto(vehicle);
    }

    @PutMapping("{id}")
    public VehicleDto updateVehicle(
            @RequestBody VehicleDto vehicleDto,
            @PathVariable("id") String id
    ) {
        Vehicle vehicle = this.vehicleService.update(id, vehicleDto);
        this.logger.info("Updating vehicle={}", vehicle);
        return this.vehicleService.convertToVehicleDto(vehicle);
    }

    @ExceptionHandler(value = VehicleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleVehicleNotFound(VehicleNotFoundException exception) {
        this.logger.error("Throwing VehicleNotFoundException with message={}", exception.getMessage());
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage()
        );
    }
}
