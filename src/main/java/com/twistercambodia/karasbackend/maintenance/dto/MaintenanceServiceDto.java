package com.twistercambodia.karasbackend.maintenance.dto;

import com.twistercambodia.karasbackend.maintenance.entity.MaintenanceService;

public class MaintenanceServiceDto {
    private String id;
    private String maintenanceId;
    private String serviceId;

    public MaintenanceServiceDto() {}

    public MaintenanceServiceDto(MaintenanceService maintenanceService) {
        System.out.println(maintenanceService);
        this.id = maintenanceService.getId();;
        this.serviceId = maintenanceService.getService().getId();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(String maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
