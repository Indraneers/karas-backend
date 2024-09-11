package com.twistercambodia.karasbackend.inventory.controller;

import com.twistercambodia.karasbackend.inventory.dto.UnitDto;
import com.twistercambodia.karasbackend.inventory.entity.Unit;
import com.twistercambodia.karasbackend.inventory.service.UnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public List<UnitDto> getAllUnits() {
        return this.unitService.convertToUnitDto(
                this.unitService.findAll()
        );
    }

    @PostMapping
    public UnitDto createUnit(
            @RequestBody UnitDto unitDto
    ) {
        Unit unit = this.unitService.create(unitDto);
        this.logger.info("Creating unit={}", unit);
        return this.unitService.convertToUnitDto(unit);
    }

    @PutMapping("{id}")
    public UnitDto updateUnit(
            @RequestBody UnitDto unitDto,
            @PathVariable("id") String id
    ) throws RuntimeException {
        Unit unit = this.unitService.update(id, unitDto);
        this.logger.info("Updating unit={}", unit);
        return this.unitService.convertToUnitDto(unit);
    }

    @DeleteMapping("{id}")
    public UnitDto deleteUnit(
            @PathVariable("id") String id
    ) throws RuntimeException {
        Unit unit = this.unitService.delete(id);
        this.logger.info("Deleting unit={}", unit);
        return this.unitService.convertToUnitDto(unit);
    }
}
