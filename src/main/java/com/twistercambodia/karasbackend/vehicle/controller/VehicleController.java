package com.twistercambodia.karasbackend.vehicle.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twistercambodia.karasbackend.audit.dto.AuditDTO;
import com.twistercambodia.karasbackend.audit.entity.Audit;
import com.twistercambodia.karasbackend.audit.entity.HttpMethod;
import com.twistercambodia.karasbackend.audit.entity.ServiceEnum;
import com.twistercambodia.karasbackend.audit.service.AuditService;
import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.vehicle.dto.VehicleDto;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import com.twistercambodia.karasbackend.vehicle.service.VehicleService;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public VehicleController(
            VehicleService vehicleService,
            AuditService auditService,
            ObjectMapper objectMapper
    ) {
        this.vehicleService = vehicleService;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public Page<VehicleDto> getAllVehicles(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam int page
    ) {
        return this.vehicleService.findAll(q, page)
                .map(vehicleService::convertToVehicleDto);
    }

    @GetMapping("{id}")
    public VehicleDto getVehicleById(@PathVariable("id") String id) {
        return this.vehicleService.convertToVehicleDto(
                this.vehicleService.findByIdOrThrowException(id)
        );
    }

    @PostMapping
    public VehicleDto createVehicle(
            @RequestBody VehicleDto vehicleDto,
            @AuthenticationPrincipal User user
    ) throws IOException {
        Vehicle vehicle = this.vehicleService.create(vehicleDto);
        this.logger.info("Creating vehicle={}", vehicle);

        VehicleDto createdVehicle = this.vehicleService.convertToVehicleDto(vehicle);

        // create audit log of Vehicle created
        AuditDTO auditDTO = new AuditDTO();

        String newValueJSON = objectMapper.writeValueAsString(createdVehicle);

        auditDTO.setOldValue(null);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Vehicle Creation");
        auditDTO.setRequestUrl("/vehicles");
        auditDTO.setService(ServiceEnum.VEHICLE);
        auditDTO.setHttpMethod(HttpMethod.POST);
        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);

        return createdVehicle;
    }

    @PutMapping("{id}")
    public VehicleDto updateVehicle(
            @RequestBody VehicleDto vehicleDto,
            @PathVariable("id") String id,
            @AuthenticationPrincipal User user
    ) throws IOException {
        Vehicle vehicle = this.vehicleService.update(id, vehicleDto);
        this.logger.info("Updating vehicle={}", vehicle);

        VehicleDto updatedVehicle = this.vehicleService.convertToVehicleDto(vehicle);

        // create audit log of Vehicle updated
        AuditDTO auditDTO = new AuditDTO();

        String newValueJSON = objectMapper.writeValueAsString(updatedVehicle);

        auditDTO.setOldValue(null);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Vehicle Update");
        auditDTO.setRequestUrl("/vehicles/" + id);
        auditDTO.setService(ServiceEnum.VEHICLE);
        auditDTO.setHttpMethod(HttpMethod.PUT);
        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);

        return this.vehicleService.convertToVehicleDto(vehicle);
    }

    @DeleteMapping("{id}")
    public VehicleDto deleteVehicle(
            @PathVariable("id") String id,
            @AuthenticationPrincipal User user
    ) throws IOException {
        Vehicle vehicle = this.vehicleService.delete(id);
        this.logger.info("Deleted vehicle={}", vehicle);

        VehicleDto deletedVehicle = this.vehicleService.convertToVehicleDto(vehicle);

        // create audit log of Vehicle updated
        AuditDTO auditDTO = new AuditDTO();

        String newValueJSON = objectMapper.writeValueAsString(deletedVehicle);

        auditDTO.setOldValue(null);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Vehicle Deletion");
        auditDTO.setRequestUrl("/vehicles/" + id);
        auditDTO.setService(ServiceEnum.VEHICLE);
        auditDTO.setHttpMethod(HttpMethod.DELETE);
        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);

        return this.vehicleService.convertToVehicleDto(vehicle);
    }
}
