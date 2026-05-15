package com.twistercambodia.karasbackend.customer.repository;

import com.twistercambodia.karasbackend.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, String> {
    /**
     * Paginated customer search across name, phone, and address with
     * trigram fuzzy ranking. Stable sort by id for paging consistency.
     * Empty query returns everything ordered by name.
     */
    @Query(value = """
        SELECT c.* FROM customer c
        WHERE
            :q = ''
            OR coalesce(c.name, '') ILIKE '%' || :q || '%'
            OR coalesce(c.contact, '') ILIKE '%' || :q || '%'
            OR coalesce(c.address, '') ILIKE '%' || :q || '%'
            OR similarity(coalesce(c.name, ''), :q) > 0.2
            OR similarity(coalesce(c.contact, ''), :q) > 0.2
        ORDER BY
            CASE WHEN :q = '' THEN 0 ELSE
                GREATEST(
                    similarity(coalesce(c.name, ''), :q) * 1.4,
                    similarity(coalesce(c.contact, ''), :q) * 1.3,
                    similarity(coalesce(c.address, ''), :q) * 0.7
                )
            END DESC,
            c.name ASC,
            c.id ASC
    """,
    countQuery = """
        SELECT count(*) FROM customer c
        WHERE
            :q = ''
            OR coalesce(c.name, '') ILIKE '%' || :q || '%'
            OR coalesce(c.contact, '') ILIKE '%' || :q || '%'
            OR coalesce(c.address, '') ILIKE '%' || :q || '%'
            OR similarity(coalesce(c.name, ''), :q) > 0.2
            OR similarity(coalesce(c.contact, ''), :q) > 0.2
    """,
    nativeQuery = true)
    Page<Customer> findAll(@Param("q") String q, Pageable pageable);

    @Query("""
        SELECT DATE(c.createdAt) AS date,
               COUNT(c) AS totalCustomers
        FROM Vehicle c
        WHERE c.createdAt >= :startDate
        GROUP BY DATE(c.createdAt)
        ORDER BY date ASC
    """)
    List<Object[]> getDailyCustomerCreation(@Param("startDate") Instant startDate);

}
