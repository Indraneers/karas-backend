package com.twistercambodia.karasbackend.vehicle.repository;

import com.twistercambodia.karasbackend.customer.entity.Customer;
import com.twistercambodia.karasbackend.sale.entity.Sale;
import com.twistercambodia.karasbackend.vehicle.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface VehicleRepository extends CrudRepository<Vehicle, String> {
    /**
     * Paginated vehicle search. Matches plate, VIN, engine, make/model, the
     * owner's name, AND the owner's contact number — so typing "1W-0473",
     * "2HGFG…", "Sokha", or "096 507 6274" all work. Ranked by trigram
     * similarity for typo tolerance, with deterministic tiebreak by id so
     * paging is stable.
     *
     * An empty query returns everything ordered by plate (cheap path).
     */
    @Query(value = """
        SELECT v.* FROM vehicle v
        LEFT JOIN customer c ON c.id = v.customer_id
        WHERE
            :q = ''
            OR coalesce(v.plate_number, '') ILIKE '%' || :q || '%'
            OR coalesce(v.vin_no, '') ILIKE '%' || :q || '%'
            OR coalesce(v.engine_no, '') ILIKE '%' || :q || '%'
            OR coalesce(v.make_and_model, '') ILIKE '%' || :q || '%'
            OR coalesce(c.name, '') ILIKE '%' || :q || '%'
            OR coalesce(c.contact, '') ILIKE '%' || :q || '%'
            OR similarity(coalesce(v.plate_number, ''), :q) > 0.2
            OR similarity(coalesce(c.name, ''), :q) > 0.2
        ORDER BY
            CASE WHEN :q = '' THEN 0 ELSE
                GREATEST(
                    similarity(coalesce(v.plate_number, ''), :q) * 1.7,
                    similarity(coalesce(v.vin_no, ''), :q) * 1.2,
                    similarity(coalesce(v.engine_no, ''), :q) * 1.1,
                    similarity(coalesce(v.make_and_model, ''), :q),
                    similarity(coalesce(c.name, ''), :q) * 1.3,
                    similarity(coalesce(c.contact, ''), :q) * 1.4
                )
            END DESC,
            v.plate_number ASC,
            v.id ASC
    """,
    countQuery = """
        SELECT count(*) FROM vehicle v
        LEFT JOIN customer c ON c.id = v.customer_id
        WHERE
            :q = ''
            OR coalesce(v.plate_number, '') ILIKE '%' || :q || '%'
            OR coalesce(v.vin_no, '') ILIKE '%' || :q || '%'
            OR coalesce(v.engine_no, '') ILIKE '%' || :q || '%'
            OR coalesce(v.make_and_model, '') ILIKE '%' || :q || '%'
            OR coalesce(c.name, '') ILIKE '%' || :q || '%'
            OR coalesce(c.contact, '') ILIKE '%' || :q || '%'
            OR similarity(coalesce(v.plate_number, ''), :q) > 0.2
            OR similarity(coalesce(c.name, ''), :q) > 0.2
    """,
    nativeQuery = true)
    Page<Vehicle> findAll(@Param("q") String q, Pageable page);

    @Query(
        """
        select v from Vehicle v where (?1 = v.customer.id)
        """
    )
    List<Vehicle> findByCustomerId(@Param("customerId") String customerId);

    @Query("""
        SELECT DATE(v.createdAt) AS date,
               COUNT(v) AS totalVehicles
        FROM Vehicle v
        WHERE v.createdAt >= :startDate
        GROUP BY DATE(v.createdAt)
        ORDER BY date ASC
    """)
    List<Object[]> getDailyVehicleCreation(@Param("startDate") Instant startDate);

}
