package com.twistercambodia.karasbackend.vehicle.service;

import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.customer.service.CustomerService;
import com.twistercambodia.karasbackend.exception.NotFoundException;
import com.twistercambodia.karasbackend.vehicle.dto.VehicleDto;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import com.twistercambodia.karasbackend.vehicle.repository.VehicleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    public VehicleService(
            VehicleRepository vehicleRepository,
            CustomerService customerService,
            ModelMapper modelMapper) {
        this.vehicleRepository = vehicleRepository;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    public Vehicle findVehicleByIdOrThrowException(String id) {
        return this.vehicleRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Vehicle Not Found with ID=" + id));
    }

    public Vehicle create(VehicleDto vehicleDto) {
        Vehicle vehicle = convertToVehicle(vehicleDto);
        Customer customer = this.customerService.findByIdOrThrowError(vehicleDto.getCustomerId());
        vehicle.setCustomer(customer);
        return this.vehicleRepository.save(vehicle);
    }

    public Vehicle update(String id, VehicleDto vehicleDto) {
        Vehicle vehicle = this.findVehicleByIdOrThrowException(id);

        vehicle.setVinNo(vehicleDto.getVinNo());
        vehicle.setEngineNo(vehicleDto.getEngineNo());
        vehicle.setMileage(vehicleDto.getMileage());
        vehicle.setNote(vehicleDto.getNote());
        vehicle.setPlateNumber(vehicleDto.getPlateNumber());
        vehicle.setMakeAndModel(vehicleDto.getMakeAndModel());

        return this.vehicleRepository.save(vehicle);
    }

    public Vehicle delete(String id) {
        Vehicle vehicle = this.findVehicleByIdOrThrowException(id);

        this.vehicleRepository.delete(vehicle);
        return vehicle;
    }

    public Vehicle convertToVehicle(VehicleDto vehicleDto) {
        return this.modelMapper.map(vehicleDto, Vehicle.class);
    }

    public VehicleDto convertToVehicleDto(Vehicle vehicle) {
        return this.modelMapper.map(vehicle, VehicleDto.class);
    }
}
