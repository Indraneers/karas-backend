package com.twistercambodia.karasbackend.inventory.controller;

import com.twistercambodia.karasbackend.inventory.dto.RestockRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.RestockResponseDto;
import com.twistercambodia.karasbackend.inventory.dto.UnitRequestDto;
import com.twistercambodia.karasbackend.inventory.dto.UnitResponseDto;
import com.twistercambodia.karasbackend.inventory.entity.Restock;
import com.twistercambodia.karasbackend.inventory.entity.Unit;
import com.twistercambodia.karasbackend.inventory.service.RestockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("restock")
public class RestockController {
    private final RestockService restockService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public RestockController(RestockService restockService) {
        this.restockService = restockService;
    }

    @GetMapping
    public List<RestockResponseDto> getAllRestocks() {
        return restockService.convertToRestockResponseDto(
                restockService.findAll()
        );
    }

    @GetMapping("{id}")
    public RestockResponseDto getRestockById(@PathVariable("id") String id) {
        return restockService.convertToRestockResponseDto(
                restockService.findByIdOrThrowError(id)
        );
    }

    @PostMapping()
    public RestockResponseDto createUnit(
            @RequestBody RestockRequestDto restockDto
            ) throws Exception {
        Restock restock = restockService.create(restockDto);
        this.logger.info("Creating restock={}", restock);
        return restockService.convertToRestockResponseDto(restock);
    }
}
