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
import java.time.Instant;
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
    List<Object[]> getDailyRevenue(@Param("startDate") Instant startDate);

    @Query("""
        SELECT i.unit.product.id AS productId,
               i.unit.product.name AS productName,
               SUM(
                   CASE
                       WHEN i.unit.product.variable = true THEN i.price * (i.quantity / 1000)
                       ELSE i.price * (i.quantity / i.unit.toBaseUnit)
                   END - i.discount
               ) AS revenue,
               COUNT(i.id) AS unitsSold
        FROM Sale s
        JOIN s.items i
        WHERE s.createdAt >= :startDate
        GROUP BY i.unit.product.id, i.unit.product.name
        ORDER BY revenue DESC
    """)
    List<Object[]> getTopProductsByRevenue(@Param("startDate") Instant startDate);

    @Query("""
        SELECT DATE(s.createdAt) AS date,
               SUM(
                   CASE
                       WHEN i.unit.product.variable = true THEN i.price * (i.quantity / 1000)
                       ELSE i.price * (i.quantity / i.unit.toBaseUnit)
                   END - i.discount
               ) AS revenue,
               COUNT(DISTINCT s.id) AS orderCount
        FROM Sale s
        JOIN s.items i
        WHERE s.createdAt >= :startDate
        GROUP BY DATE(s.createdAt)
        ORDER BY date ASC
    """)
    List<Object[]> getDailyAverageOrderValue(@Param("startDate") Instant startDate);

    @Query("""
        SELECT s.customer.id AS customerId,
               COALESCE(s.customer.name, 'Walk-in') AS customerName,
               SUM(
                   CASE
                       WHEN i.unit.product.variable = true THEN i.price * (i.quantity / 1000)
                       ELSE i.price * (i.quantity / i.unit.toBaseUnit)
                   END - i.discount
               ) AS revenue,
               COUNT(DISTINCT s.id) AS orderCount
        FROM Sale s
        JOIN s.items i
        WHERE s.createdAt >= :startDate
        GROUP BY s.customer.id, s.customer.name
        ORDER BY revenue DESC
    """)
    List<Object[]> getTopCustomersByRevenue(@Param("startDate") Instant startDate);

    @Query("""
        SELECT s.paymentType AS paymentType,
               COUNT(DISTINCT s.id) AS orderCount,
               SUM(
                   CASE
                       WHEN i.unit.product.variable = true THEN i.price * (i.quantity / 1000)
                       ELSE i.price * (i.quantity / i.unit.toBaseUnit)
                   END - i.discount
               ) AS revenue
        FROM Sale s
        JOIN s.items i
        WHERE s.createdAt >= :startDate
        GROUP BY s.paymentType
    """)
    List<Object[]> getPaymentTypeBreakdown(@Param("startDate") Instant startDate);

    @Query("""
        SELECT s.user.id AS userId,
               COALESCE(s.user.username, 'Unknown') AS username,
               SUM(
                   CASE
                       WHEN i.unit.product.variable = true THEN i.price * (i.quantity / 1000)
                       ELSE i.price * (i.quantity / i.unit.toBaseUnit)
                   END - i.discount
               ) AS revenue,
               COUNT(DISTINCT s.id) AS orderCount
        FROM Sale s
        JOIN s.items i
        WHERE s.createdAt >= :startDate
        GROUP BY s.user.id, s.user.username
        ORDER BY revenue DESC
    """)
    List<Object[]> getRevenueByStaff(@Param("startDate") Instant startDate);
}
