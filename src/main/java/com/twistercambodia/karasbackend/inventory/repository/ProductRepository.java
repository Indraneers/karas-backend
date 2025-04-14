package com.twistercambodia.karasbackend.inventory.repository;

import com.twistercambodia.karasbackend.inventory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, String> {
    @Query(
        """
        select p from Product p
        where
            (
                (?1 is null or lower(cast(p.name as string)) like lower(concat('%', concat(cast(?1 as string), '%'))))
                OR 
                (?1 is null or lower(cast(p.identifier as string)) like lower(concat('%', concat(cast(?1 as string), '%'))))
             )
            AND
            (?2 is null or p.subcategory.id = ?2)
        """
    )
    Page<Product> findAll(String q, String subcategoryId, Pageable pageable);
}
