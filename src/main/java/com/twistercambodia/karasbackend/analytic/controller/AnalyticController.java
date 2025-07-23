package com.twistercambodia.karasbackend.analytic.controller;

import com.twistercambodia.karasbackend.analytic.dto.AnalyticDto;
import com.twistercambodia.karasbackend.analytic.service.AnalyticService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("analytics")
public class AnalyticController {
    private final AnalyticService analyticService;
    private static final ZoneId PHNOM_PENH_ZONE = ZoneId.of("Asia/Phnom_Penh");

    public AnalyticController(AnalyticService analyticService) {
        this.analyticService = analyticService;
    }

    @GetMapping("sales/week")
    public List<AnalyticDto> totalSalesThisWeek() {
        Instant oneWeekAgo = ZonedDateTime.now(PHNOM_PENH_ZONE)
                .minus(1, ChronoUnit.WEEKS)
                .toInstant();
        return analyticService.getTotalSalesFromDate(oneWeekAgo);
    }

    @GetMapping("sales/month")
    public List<AnalyticDto> totalSalesThisMonth() {
        Instant oneMonthAgo = ZonedDateTime.now(PHNOM_PENH_ZONE)
                .minus(1, ChronoUnit.MONTHS)
                .toInstant();
        return analyticService.getTotalSalesFromDate(oneMonthAgo);
    }

    @GetMapping("vehicles/week")
    public List<AnalyticDto> totalVehiclesThisWeek() {
        Instant oneWeekAgo = ZonedDateTime.now(PHNOM_PENH_ZONE)
                .minus(1, ChronoUnit.WEEKS)
                .toInstant();
        return analyticService.getTotalVehiclesFromDate(oneWeekAgo);
    }

    @GetMapping("vehicles/month")
    public List<AnalyticDto> totalVehiclesThisMonth() {
        Instant oneMonthAgo = ZonedDateTime.now(PHNOM_PENH_ZONE)
                .minus(1, ChronoUnit.MONTHS)
                .toInstant();
        return analyticService.getTotalVehiclesFromDate(oneMonthAgo);
    }

    @GetMapping("customers/week")
    public List<AnalyticDto> totalCustomersThisWeek() {
        Instant oneWeekAgo = ZonedDateTime.now(PHNOM_PENH_ZONE)
                .minus(1, ChronoUnit.WEEKS)
                .toInstant();
        return analyticService.getTotalCustomersFromDate(oneWeekAgo);
    }

    @GetMapping("customers/month")
    public List<AnalyticDto> totalCustomersThisMonth() {
        Instant oneMonthAgo = ZonedDateTime.now(PHNOM_PENH_ZONE)
                .minus(1, ChronoUnit.MONTHS)
                .toInstant();
        return analyticService.getTotalVehiclesFromDate(oneMonthAgo);
    }

    // Helper methods to reduce code duplication (optional refactor)
    private Instant getDateInPhnomPenhTime(long amount, ChronoUnit unit) {
        return ZonedDateTime.now(PHNOM_PENH_ZONE)
                .minus(amount, unit)
                .toInstant();
    }
}