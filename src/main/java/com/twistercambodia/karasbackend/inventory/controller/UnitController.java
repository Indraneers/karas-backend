package com.twistercambodia.karasbackend.inventory.controller;

import com.twistercambodia.karasbackend.inventory.dto.UnitRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.UnitResponseDto;
import com.twistercambodia.karasbackend.inventory.entity.Unit;
import com.twistercambodia.karasbackend.inventory.service.UnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("units")
public class UnitController {
    private final UnitService unitService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
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
            @RequestBody UnitRequestDto unitRequestDto
    ) {
        Unit unit = this.unitService.create(unitRequestDto);
        this.logger.info("Creating unit={}", unit);
        return this.unitService.convertToUnitDto(unit);
    }

    @PutMapping("{id}")
    public UnitResponseDto updateUnit(
            @RequestBody UnitRequestDto unitRequestDto,
            @PathVariable("id") String id
    ) throws RuntimeException {
        Unit unit = this.unitService.update(id, unitRequestDto);
        this.logger.info("Updating unit={}", unit);
        return this.unitService.convertToUnitDto(unit);
    }

    @DeleteMapping("{id}")
    public UnitResponseDto deleteUnit(
            @PathVariable("id") String id
    ) throws RuntimeException {
        Unit unit = this.unitService.delete(id);
        this.logger.info("Deleting unit={}", unit);
        return this.unitService.convertToUnitDto(unit);
    }
}
