package com.twistercambodia.karasbackend.sale.repository;

import com.twistercambodia.karasbackend.analytic.dto.AnalyticDto;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends CrudRepository<Sale, Long>, JpaSpecificationExecutor<Sale> {
    Page<Sale> findAll(Specification<Sale> specification, Pageable pageable);

    @Query("""
        SELECT DATE(s.createdAt) AS date, 
               SUM(
                   CASE 
                       WHEN i.unit.product.variable = true THEN i.price * (i.quantity / 1000)
                       ELSE i.price * (i.quantity / i.unit.toBaseUnit)
                   END - i.discount
               ) AS totalRevenue
        FROM Sale s 
        JOIN s.items i 
        WHERE s.createdAt >= :startDate
        GROUP BY DATE(s.createdAt)
        ORDER BY date ASC
    """)
    List<Object[]> getDailyRevenue(@Param("startDate") LocalDateTime startDate);

}
