// Updated AnalyticService with corrected aggregation logic
package com.twistercambodia.karasbackend.analytic.service;

import com.twistercambodia.karasbackend.analytic.dto.AnalyticDto;
import com.twistercambodia.karasbackend.customer.repository.CustomerRepository;
import com.twistercambodia.karasbackend.sale.repository.SaleRepository;
import com.twistercambodia.karasbackend.vehicle.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticService {
    private final SaleRepository saleRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private static final ZoneId PHNOM_PENH_ZONE = ZoneId.of("Asia/Phnom_Penh");

    public AnalyticService(SaleRepository saleRepository,
                           VehicleRepository vehicleRepository,
                           CustomerRepository customerRepository) {
        this.saleRepository = saleRepository;
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
    }

    private Map<Instant, Integer> convertRawObjectsToMap(List<Object[]> rawObjects) {
        return rawObjects.stream()
                .collect(Collectors.toMap(
                        row -> {
                            Object dateObj = row[0];
                            if (dateObj instanceof java.sql.Date) {
                                // Convert java.sql.Date to start of day as Instant (UTC)
                                return ((java.sql.Date) dateObj).toLocalDate()
                                        .atStartOfDay()
                                        .toInstant(java.time.ZoneOffset.UTC);
                            } else if (dateObj instanceof java.sql.Timestamp) {
                                // Convert Timestamp directly to Instant
                                return ((java.sql.Timestamp) dateObj).toInstant();
                            } else {
                                throw new IllegalArgumentException("Unexpected date type: " + dateObj.getClass());
                            }
                        },
                        row -> ((Long) row[1]).intValue()
                ));
    }

    private List<AnalyticDto> aggregateFromStartDate(Instant startDateTime, Map<Instant, Integer> dataMap) {
        List<AnalyticDto> dtos = new ArrayList<>();

        // Start from the beginning of the start date (truncate to day)
        Instant current = startDateTime.truncatedTo(ChronoUnit.DAYS);

        // Get current time truncated to day
        Instant endDate = Instant.now().truncatedTo(ChronoUnit.DAYS);

        // Loop through each day from start date to current date
        while (!current.isAfter(endDate)) {
            // Get the value from the map using the current Instant key
            int value = dataMap.getOrDefault(current, 0);

            // Convert Instant to LocalDate string for display
            String dateString = current.atZone(PHNOM_PENH_ZONE).toLocalDate().toString();

            // Add to result list
            dtos.add(new AnalyticDto(dateString, value));

            // Move to next day
            current = current.plus(1, ChronoUnit.DAYS);
        }

        return dtos;
    }

    public List<AnalyticDto> getTotalSalesFromDate(Instant startDateTime) {
        List<Object[]> rawObjects = this.saleRepository.getDailyRevenue(startDateTime);
        Map<Instant, Integer> revenueMap = convertRawObjectsToMap(rawObjects);
        return aggregateFromStartDate(startDateTime, revenueMap);
    }

    public List<AnalyticDto> getTotalVehiclesFromDate(Instant startDateTime) {
        List<Object[]> rawObjects = this.vehicleRepository.getDailyVehicleCreation(startDateTime);
        Map<Instant, Integer> dataMap = convertRawObjectsToMap(rawObjects);
        return aggregateFromStartDate(startDateTime, dataMap);
    }

    public List<AnalyticDto> getTotalCustomersFromDate(Instant startDateTime) {
        List<Object[]> rawObjects = this.customerRepository.getDailyCustomerCreation(startDateTime);
        Map<Instant, Integer> dataMap = convertRawObjectsToMap(rawObjects);
        return aggregateFromStartDate(startDateTime, dataMap);
    }
}