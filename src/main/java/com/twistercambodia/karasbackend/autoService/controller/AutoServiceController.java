package com.twistercambodia.karasbackend.autoService.controller;

import com.twistercambodia.karasbackend.autoService.dto.AutoServiceDto;
import com.twistercambodia.karasbackend.autoService.service.AutoServiceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("auto-services")
public class AutoServiceController {
    private final AutoServiceService autoServiceService;

    public AutoServiceController(AutoServiceService autoServiceService) {
        this.autoServiceService = autoServiceService;
    }

    @GetMapping
    public List<AutoServiceDto> getAllServices() {
        return this.autoServiceService.convertToAutoServiceDto(
                this.autoServiceService.findAll()
        );
    }
}
