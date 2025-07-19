package com.twistercambodia.karasbackend.customer.repository;

import com.twistercambodia.karasbackend.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, String> {
    @Query("""
        select c from Customer c
        where 
            (?1 is null or lower(cast(c.name as string)) like lower(concat('%', concat(cast(?1 as string), '%'))))
    """)
    Page<Customer> findAll(String q, Pageable pageable);

    @Query("""
        SELECT DATE(c.createdAt) AS date, 
               COUNT(c) AS totalCustomers
        FROM Vehicle c
        WHERE c.createdAt >= :startDate
        GROUP BY DATE(c.createdAt)
        ORDER BY date ASC
    """)
    List<Object[]> getDailyCustomerCreation(@Param("startDate") LocalDateTime startDate);
}
