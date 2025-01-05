package com.twistercambodia.karasbackend.maintenance.service;

import com.twistercambodia.karasbackend.exception.exceptions.NotFoundException;
import com.twistercambodia.karasbackend.maintenance.dto.MaintenanceDto;
import com.twistercambodia.karasbackend.maintenance.dto.MaintenanceAutoServiceDto;
import com.twistercambodia.karasbackend.maintenance.entity.Maintenance;
import com.twistercambodia.karasbackend.maintenance.entity.MaintenanceAutoService;
import com.twistercambodia.karasbackend.maintenance.repository.MaintenanceRepository;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import com.twistercambodia.karasbackend.vehicle.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {
    private final ModelMapper modelMapper;
    private final MaintenanceRepository maintenanceRepository;
    private final VehicleService vehicleService;

    public MaintenanceService(MaintenanceRepository maintenanceRepository, VehicleService vehicleService, ModelMapper modelMapper) {
        this.maintenanceRepository = maintenanceRepository;
        this.vehicleService = vehicleService;
        this.modelMapper = modelMapper;
    }

    public List<Maintenance> findAll() {
        return this.maintenanceRepository.findAll();
    }

    public Maintenance findByIdOrThrowError(String id) throws RuntimeException {
        return this.maintenanceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Maintenance Not Found with ID=" + id));
    }

    public Maintenance create(MaintenanceDto maintenanceDto) {
        Maintenance maintenance = convertToMaintenance(maintenanceDto);
        System.out.println("CALLED - 1");
        System.out.println(maintenanceDto.getCreatedAt() + " " + LocalDateTime.parse(maintenanceDto.getCreatedAt()));
        System.out.println("CALLED - 2");
        maintenance.setCreatedAt(LocalDateTime.parse(maintenanceDto.getCreatedAt()));
        maintenance.getServices().forEach(ms -> ms.setMaintenance(maintenance));

        return maintenanceRepository.save(maintenance);
    }

    public Maintenance update(String id, MaintenanceDto maintenanceDto) {
        Maintenance maintenance = findByIdOrThrowError(id);

        Vehicle vehicle = vehicleService.findByIdOrThrowException(maintenanceDto.getVehicleId());

        maintenance.setCreatedAt(LocalDateTime.parse(maintenanceDto.getCreatedAt()));
        maintenance.setMileage(maintenanceDto.getMileage());
        maintenance.setNote(maintenanceDto.getNote());
        maintenance.setVehicle(vehicle);

        maintenance.getServices().clear();

        Set<MaintenanceAutoService> maintenanceAutoServices =
                maintenanceDto.getServices()
                        .stream()
                        .map(ma -> modelMapper.map(ma, MaintenanceAutoService.class))
                        .collect(Collectors.toSet());

        maintenanceAutoServices.forEach(ma -> ma.setMaintenance(maintenance));

        maintenance.getServices().addAll(maintenanceAutoServices);

        return maintenanceRepository.save(maintenance);
    }

    public Maintenance convertToMaintenance(MaintenanceDto maintenanceDto) {
        return this.modelMapper.map(maintenanceDto, Maintenance.class);
    }

    public MaintenanceDto convertToMaintenanceDto(Maintenance maintenance) {
        return this.modelMapper.map(maintenance, MaintenanceDto.class);
    }

    public List<MaintenanceDto> convertToMaintenanceDto(List<Maintenance> maintenances) {
        return maintenances
                .stream()
                .map((m) -> this.modelMapper.map(m, MaintenanceDto.class))
                .collect(Collectors.toList());
    }

    public MaintenanceAutoServiceDto convertToMaintenanceServiceDto(
            MaintenanceAutoService maintainService) {
        return modelMapper.map(maintainService, MaintenanceAutoServiceDto.class);
    }

    public Set<MaintenanceAutoServiceDto> convertToMaintenanceServiceDto(
            Set<MaintenanceAutoService>
                    maintenanceAutoServices) {
        return maintenanceAutoServices
                .stream()
                .map(this::convertToMaintenanceServiceDto)
                .collect(Collectors.toSet());
    }

    public MaintenanceAutoService convertToMaintenanceService(
            MaintenanceAutoServiceDto maintainServiceDto) {
        return modelMapper.map(maintainServiceDto, MaintenanceAutoService.class);
    }

    public Set<MaintenanceAutoService> convertToMaintenanceService(
            Set<MaintenanceAutoServiceDto>
                    maintenanceAutoServiceDtos) {
        return maintenanceAutoServiceDtos
                .stream()
                .map(this::convertToMaintenanceService)
                .collect(Collectors.toSet());
    }
}
