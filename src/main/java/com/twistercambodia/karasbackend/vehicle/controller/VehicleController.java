package com.twistercambodia.karasbackend.vehicle.controller;

import com.twistercambodia.karasbackend.vehicle.dto.VehicleDto;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import com.twistercambodia.karasbackend.vehicle.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<VehicleDto> getAllVehicles(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "customerId", required = false) String customerId
    ) {
        if (customerId != null) {
            return this.vehicleService.convertToVehicleDto(
                    this.vehicleService.findByCustomerId(customerId)
            );
        }
        return this.vehicleService.convertToVehicleDto(
          this.vehicleService.findAll(q)
        );
    }

    @GetMapping("{id}")
    public VehicleDto getVehicleById(@PathVariable("id") String id) {
        return this.vehicleService.convertToVehicleDto(
                this.vehicleService.findByIdOrThrowException(id)
        );
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

    @DeleteMapping("{id}")
    public VehicleDto deleteVehicle(
            @PathVariable("id") String id
    ) {
        Vehicle vehicle = this.vehicleService.delete(id);
        return this.vehicleService.convertToVehicleDto(vehicle);
    }
}
