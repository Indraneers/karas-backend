package com.twistercambodia.karasbackend.inventory.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twistercambodia.karasbackend.audit.dto.AuditDTO;
import com.twistercambodia.karasbackend.audit.entity.Audit;
import com.twistercambodia.karasbackend.audit.entity.HttpMethod;
import com.twistercambodia.karasbackend.audit.entity.ServiceEnum;
import com.twistercambodia.karasbackend.audit.service.AuditService;
import com.twistercambodia.karasbackend.auth.entity.User;
import com.twistercambodia.karasbackend.inventory.dto.ProductResponseDto;
import com.twistercambodia.karasbackend.inventory.dto.UnitRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.UnitResponseDto;
import com.twistercambodia.karasbackend.inventory.entity.Unit;
import com.twistercambodia.karasbackend.inventory.service.UnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("units")
public class UnitController {
    private final UnitService unitService;
    private final AuditService auditService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public UnitController(
            UnitService unitService,
            AuditService auditService,
            ObjectMapper objectMapper
    ) {
        this.unitService = unitService;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public Page<UnitResponseDto> getAllUnits(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "productId", required = false) String productId,
            @RequestParam(value = "page", required = true) int page
    ) {
        return this.unitService.findAll(q, productId, page)
                .map(UnitResponseDto::new);
    }

    @GetMapping("{id}")
    public UnitResponseDto getUnitById(@PathVariable("id") String id) throws RuntimeException {
        return this.unitService.convertToUnitDto(
                this.unitService.findByIdOrThrowError(id)
        );
    }

    @PostMapping
    public UnitResponseDto createUnit(
            @RequestBody UnitRequestDto unitRequestDto,
            @AuthenticationPrincipal User user
    ) throws IOException {
        Unit unit = this.unitService.create(unitRequestDto);
        this.logger.info("Creating unit={}", unit);

        UnitResponseDto unitResponseDto =
                this.unitService.convertToUnitDto(unit);

        // create audit log of Product Deletion
        AuditDTO auditDTO = new AuditDTO();

        String newValueJSON = objectMapper.writeValueAsString(unitResponseDto);

        auditDTO.setOldValue(null);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Unit Creation");
        auditDTO.setRequestUrl("/units");
        auditDTO.setService(ServiceEnum.UNIT);
        auditDTO.setHttpMethod(HttpMethod.POST);
        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);
        this.logger.info("Adding audit log for unit={}", audit);

        return unitResponseDto;
    }

    @PutMapping("{id}")
    public UnitResponseDto updateUnit(
            @RequestBody UnitRequestDto unitRequestDto,
            @PathVariable("id") String id,
            @AuthenticationPrincipal User user
    ) throws RuntimeException, IOException {
        Unit unit = this.unitService.update(id, unitRequestDto);
        this.logger.info("Updating unit={}", unit);

        UnitResponseDto unitResponseDto =
                this.unitService.convertToUnitDto(unit);

        // create audit log of Product Deletion
        AuditDTO auditDTO = new AuditDTO();

        String newValueJSON = objectMapper.writeValueAsString(unitResponseDto);

        auditDTO.setOldValue(null);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Unit Update");
        auditDTO.setRequestUrl("/units/" + id);
        auditDTO.setService(ServiceEnum.UNIT);
        auditDTO.setHttpMethod(HttpMethod.PUT);
        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);
        this.logger.info("Adding audit log for unit={}", audit);

        return unitResponseDto;
    }

    @DeleteMapping("{id}")
    public UnitResponseDto deleteUnit(
            @PathVariable("id") String id,
            @AuthenticationPrincipal User user
    ) throws RuntimeException, IOException {
        Unit unit = this.unitService.delete(id);
        this.logger.info("Deleting unit={}", unit);

        UnitResponseDto unitResponseDto =
                this.unitService.convertToUnitDto(unit);

        // create audit log of Product Deletion
        AuditDTO auditDTO = new AuditDTO();

        String newValueJSON = objectMapper.writeValueAsString(unitResponseDto);

        auditDTO.setOldValue(null);
        auditDTO.setNewValue(newValueJSON);

        auditDTO.setName("Unit Deletion");
        auditDTO.setRequestUrl("/units/" + id);
        auditDTO.setService(ServiceEnum.UNIT);
        auditDTO.setHttpMethod(HttpMethod.DELETE);
        auditDTO.setUser(user);

        Audit audit = this.auditService.create(auditDTO);
        this.logger.info("Adding audit log for unit={}", audit);

        return unitResponseDto;
    }
}
