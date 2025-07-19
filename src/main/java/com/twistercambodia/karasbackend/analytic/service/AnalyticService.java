package com.twistercambodia.karasbackend.analytic.service;

import com.twistercambodia.karasbackend.analytic.dto.AnalyticDto;
import com.twistercambodia.karasbackend.customer.repository.CustomerRepository;
import com.twistercambodia.karasbackend.sale.repository.SaleRepository;
import com.twistercambodia.karasbackend.vehicle.repository.VehicleRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticService {
    private final SaleRepository saleRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;

    public AnalyticService(SaleRepository saleRepository,
                           VehicleRepository vehicleRepository,
                           CustomerRepository customerRepository) {
        this.saleRepository = saleRepository;
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
    }

    private Map<LocalDate, Integer> convertRawObjectsToMap(List<Object[]> rawObjects) {
        return rawObjects.stream()
                .collect(Collectors.toMap(
                        row -> ((java.sql.Date) row[0]).toLocalDate(),
                        row -> ((Long) row[1]).intValue()
                ));
    }

    public List<AnalyticDto> getTotalSalesFromDate(LocalDateTime startDateTime) {
        List<Object[]> rawObjects = this.saleRepository.getDailyRevenue(startDateTime);

        // Step 1: Map of LocalDate -> revenue
        Map<LocalDate, Integer> revenueMap = convertRawObjectsToMap(rawObjects);

        // Step 2: Loop through LocalDate only
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = LocalDate.now();

        List<AnalyticDto> result = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            int value = revenueMap.getOrDefault(date, 0);
            result.add(new AnalyticDto(date.toString(), value));
        }

        return result;
    }

    public List<AnalyticDto> getTotalVehiclesFromDate(LocalDateTime startDateTime) {
        List<Object[]> rawObjects = this.vehicleRepository.getDailyVehicleCreation(startDateTime);

        // Step 1: Map of LocalDate -> revenue
        Map<LocalDate, Integer> revenueMap = convertRawObjectsToMap(rawObjects);

        // Step 2: Loop through LocalDate only
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = LocalDate.now();

        List<AnalyticDto> result = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            int value = revenueMap.getOrDefault(date, 0);
            result.add(new AnalyticDto(date.toString(), value));
        }

        return result;
    }

    public List<AnalyticDto> getTotalCustomersFromDate(LocalDateTime startDateTime) {
        List<Object[]> rawObjects = this.customerRepository.getDailyCustomerCreation(startDateTime);

        // Step 1: Map of LocalDate -> revenue
        Map<LocalDate, Integer> revenueMap = convertRawObjectsToMap(rawObjects);

        // Step 2: Loop through LocalDate only
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = LocalDate.now();

        List<AnalyticDto> result = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            int value = revenueMap.getOrDefault(date, 0);
            result.add(new AnalyticDto(date.toString(), value));
        }

        return result;
    }
}
