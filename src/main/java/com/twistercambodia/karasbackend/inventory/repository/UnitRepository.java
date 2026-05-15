package com.twistercambodia.karasbackend.inventory.repository;

import com.twistercambodia.karasbackend.inventory.entity.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnitRepository extends CrudRepository<Unit, String> {
    @Query(
            """
            select u from Unit u
            where
                (
                    (?1 is null or lower(cast(u.name as string)) like lower(concat('%', concat(cast(?1 as string), '%'))))
                    OR
                    (?1 is null or lower(cast(u.product.identifier as string)) like lower(concat('%', concat(cast(?1 as string), '%'))))
                    OR
                    (?1 is null or lower(cast(u.product.name as string)) like lower(concat('%', concat(cast(?1 as string), '%'))))
                )
                AND
                (?2 is null or u.product.id = ?2)
            order by u.product.id, u.toBaseUnit, u.name asc
            """
    )
    Page<Unit> findAll(String q, String productId, Pageable pageable);

    /**
     * Fuzzy trigram-ranked POS search across unit, product, identifier, and
     * subcategory names. Requires the pg_trgm extension (see V0.0.16).
     *
     * Returns the top matches ordered by a weighted similarity score so that
     * a hit on the product name outweighs a hit on the unit's package name.
     */
    @Query(value = """
        SELECT u.* FROM unit u
        JOIN product p ON p.id = u.product_id
        JOIN subcategory s ON s.id = p.subcategory_id
        WHERE
            similarity(p.name, :q) > 0.15
            OR similarity(coalesce(p.identifier, ''), :q) > 0.2
            OR similarity(u.name, :q) > 0.2
            OR similarity(s.name, :q) > 0.2
            OR p.name ILIKE '%' || :q || '%'
            OR coalesce(p.identifier, '') ILIKE '%' || :q || '%'
            OR u.name ILIKE '%' || :q || '%'
        ORDER BY GREATEST(
            similarity(p.name, :q) * 1.5,
            similarity(coalesce(p.identifier, ''), :q) * 1.3,
            similarity(u.name, :q),
            similarity(s.name, :q) * 0.8
        ) DESC,
        p.name ASC, u.to_base_unit ASC, u.name ASC
        LIMIT :limit
    """, nativeQuery = true)
    List<Unit> fuzzySearch(@Param("q") String q, @Param("limit") int limit);
}
