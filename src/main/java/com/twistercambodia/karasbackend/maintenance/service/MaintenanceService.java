package com.twistercambodia.karasbackend.maintenance.service;

import com.twistercambodia.karasbackend.maintenance.dto.MaintenanceDto;
import com.twistercambodia.karasbackend.maintenance.entity.Maintenance;
import com.twistercambodia.karasbackend.maintenance.repository.MaintenanceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {
    private final ModelMapper modelMapper;
    private final MaintenanceRepository maintenanceRepository;

    public MaintenanceService(MaintenanceRepository maintenanceRepository, ModelMapper modelMapper) {
        this.maintenanceRepository = maintenanceRepository;
        this.modelMapper = modelMapper;
    }

    public List<Maintenance> findAll() {
        return this.maintenanceRepository.findAll();
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
}
