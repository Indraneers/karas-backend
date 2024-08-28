package com.twistercambodia.karasbackend.maintenance.service;

import com.twistercambodia.karasbackend.maintenance.entity.Maintenance;
import com.twistercambodia.karasbackend.maintenance.repository.MaintenanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceService {
    private MaintenanceRepository maintenanceRepository;

    public MaintenanceService(MaintenanceRepository maintenanceRepository) {
        this.maintenanceRepository = maintenanceRepository;
    }

    public List<Maintenance> findAll() {
        return this.maintenanceRepository.findAll();
    }
}
