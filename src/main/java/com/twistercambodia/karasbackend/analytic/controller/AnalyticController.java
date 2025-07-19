package com.twistercambodia.karasbackend.analytic.controller;

import com.twistercambodia.karasbackend.analytic.dto.AnalyticDto;
import com.twistercambodia.karasbackend.analytic.service.AnalyticService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("analytics")
public class AnalyticController {
    private final AnalyticService analyticService;

    public AnalyticController(AnalyticService analyticService) {
        this.analyticService = analyticService;
    }

    @GetMapping("sales/week")
    public List<AnalyticDto> totalSalesThisWeek() {
        return analyticService.getTotalSalesFromDate(LocalDateTime.now().minusWeeks(1));
    }

    @GetMapping("sales/month")
    public List<AnalyticDto> totalSalesThisMonth() {
        return analyticService.getTotalSalesFromDate(LocalDateTime.now().minusMonths(1));
    }

    @GetMapping("vehicles/week")
    public List<AnalyticDto> totalVehiclesThisWeek() {
        return analyticService.getTotalVehiclesFromDate(LocalDateTime.now().minusWeeks(1));
    }

    @GetMapping("vehicles/month")
    public List<AnalyticDto> totalMonthsThisWeek() {
        return analyticService.getTotalVehiclesFromDate(LocalDateTime.now().minusMonths(1));
    }
}
