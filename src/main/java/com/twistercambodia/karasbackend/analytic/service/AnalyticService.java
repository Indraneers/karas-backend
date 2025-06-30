package com.twistercambodia.karasbackend.analytic.service;

import com.twistercambodia.karasbackend.analytic.dto.AnalyticDto;
import com.twistercambodia.karasbackend.sale.repository.SaleRepository;
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

    public AnalyticService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public List<AnalyticDto> getTotalSalesFromDate(LocalDateTime startDateTime) {
        List<Object[]> rawObjects = this.saleRepository.getDailyRevenue(startDateTime);

        // Step 1: Map of LocalDate -> revenue
        Map<LocalDate, Integer> revenueMap = rawObjects.stream()
                .collect(Collectors.toMap(
                        row -> ((java.sql.Date) row[0]).toLocalDate(),
                        row -> ((Long) row[1]).intValue()
                ));

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
