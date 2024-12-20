package com.twistercambodia.karasbackend.maintenance.controller;

import com.twistercambodia.karasbackend.maintenance.dto.MaintenanceDto;
import com.twistercambodia.karasbackend.maintenance.entity.Maintenance;
import com.twistercambodia.karasbackend.maintenance.service.MaintenanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("maintenances")
public class MaintenanceController {
    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @GetMapping
    public List<MaintenanceDto> getAllMaintenances() {
        return this.maintenanceService.convertToMaintenanceDto(
                this.maintenanceService.findAll()
        );
    }
}
