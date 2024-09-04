package com.twistercambodia.karasbackend.autoService.controller;

import com.twistercambodia.karasbackend.autoService.dto.AutoServiceDto;
import com.twistercambodia.karasbackend.autoService.entity.AutoService;
import com.twistercambodia.karasbackend.autoService.service.AutoServiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("auto-services")
public class AutoServiceController {
    private final AutoServiceService autoServiceService;

    public AutoServiceController(AutoServiceService autoServiceService) {
        this.autoServiceService = autoServiceService;
    }

    @GetMapping
    public List<AutoServiceDto> getAllAutoServices() {
        return this.autoServiceService.convertToAutoServiceDto(
                this.autoServiceService.findAll()
        );
    }

    @PostMapping
    public AutoServiceDto createAutoService(
            @RequestBody AutoServiceDto autoServiceDto
    ) {
        AutoService autoService = this.autoServiceService.create(autoServiceDto);
        return this.autoServiceService.convertToAutoServiceDto(autoService);
    }
}
