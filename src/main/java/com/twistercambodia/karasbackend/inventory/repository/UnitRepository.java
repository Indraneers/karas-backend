package com.twistercambodia.karasbackend.inventory.repository;

import com.twistercambodia.karasbackend.inventory.entity.Unit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UnitRepository extends CrudRepository<Unit, String> {
    @Query(
            """
            select u from Unit u
            where
                (?1 is null or lower(cast(u.name as string)) like lower(concat('%', concat(cast(?1 as string), '%'))))
                AND
                (?1 is null or lower(cast(u.sku as string)) like lower(concat('%', concat(cast(?1 as string), '%'))))
                AND
                (?2 is null or u.product.id = ?2)
            """
    )
    List<Unit> findAll(String q, String productId);
}
